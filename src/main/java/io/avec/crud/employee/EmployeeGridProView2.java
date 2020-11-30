package io.avec.crud.employee;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import io.avec.crud.department.Department;
import io.avec.crud.department.DepartmentRepository;
import io.avec.crud.main.MainView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "employee-grid2", layout = MainView.class)
@PageTitle("Employee GridPro")
@CssImport("./styles/views/employee/employee-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class EmployeeGridProView2 extends Div {

    private final String firstNameHeader = "First name";
    private final String emailHeader = "Email";
    private final String departmentHeader = "Department";

    private final DepartmentRepository departmentRepository;
    private Grid.Column<Employee> firstNameColumn;
    private Grid.Column<Employee> emailColumn;
    private Grid.Column<Employee> departmentColumn;

    private Employee lastOpenedDetailsEmployee = null;
    private final GridPro<Employee> grid = new GridPro<>();
    private final ListDataProvider<Employee> dataProvider;
    private HeaderRow filterRow;


    public EmployeeGridProView2(EmployeeCrudService service, DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
        setId("employee-view");
        Paragraph paragraph = new Paragraph("You can edit inside every cell. Database will be updated automatically.");
        Checkbox editableCheckbox = new Checkbox("Edit modus");


//        GridPro<Employee> grid = new GridPro<>();
//        GridPro<Employee> grid = new GridPro<>(Employee.class);
        grid.getStyle().set("position", "fixed"); // again why?
//        grid.setMultiSort(true); // multi sort columns

        VerticalLayout layout = new VerticalLayout(paragraph, new Div(editableCheckbox), new Div(grid));
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setMargin(false);
        add(layout);

        dataProvider = new ListDataProvider<>(service.getRepository().findAll());
        grid.setDataProvider(dataProvider);


        // default edit mode
        editableCheckbox.setValue(true);
        addEditColumns(grid);


        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        grid.setWidthFull();

        grid.addItemPropertyChangedListener(e -> {
            service.update(e.getItem());
            Notification.show("Change stored.", 3000, Notification.Position.TOP_CENTER);
        });


        filterRow = addFilters();

//        grid.setSingleCellEdit(false);
        grid.addItemClickListener(e -> {
            lastOpenedDetailsEmployee = e.getItem();
            Notification.show("Viewing " + lastOpenedDetailsEmployee.getFirstName() + " details.");
            //grid.setDetailsVisible(employeeItemClickEvent.getItem(), true);
        });

        grid.setDetailsVisibleOnClick(false);
//        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setItemDetailsRenderer(TemplateRenderer.<Employee>of("""
                <div style='border: 1px solid gray; padding: 10px; width: 100%; box-sizing: border-box;'>
                    <div>Hi! My name is <b>[[item.firstName]]!</b></div>
                </div>""")
                .withProperty("firstName", Employee::getFirstName)
                .withEventHandler("handleClick", employee -> grid.getDataProvider().refreshItem(employee))

        );

        editableCheckbox.addValueChangeListener(e -> {

//            grid.removeColumns(firstNameColumn, emailColumn, departmentColumn);
            grid.removeAllColumns();
            grid.getHeaderRows().remove(filterRow);
            boolean isEditModus = e.getValue();
            if(isEditModus) {
                if(lastOpenedDetailsEmployee != null) {
                    grid.setDetailsVisible(lastOpenedDetailsEmployee, false);
                }
                grid.setDetailsVisibleOnClick(false);
                addEditColumns(grid);
                addFilters();
                Notification.show("Edit modus enabled.");
            } else {
                // Now we can view details
                firstNameColumn = grid.addColumn(Employee::getFirstName, "firstName")
                        .setHeader(firstNameHeader);
                emailColumn = grid.addColumn(Employee::getEmail, "email")
                        .setHeader(emailHeader);
                departmentColumn = grid.addColumn(employee ->
                        employee.getDepartment().getDepartmentName(), "employee.department.departmentName")
                        .setHeader(departmentHeader);
                grid.setDetailsVisibleOnClick(true);
                Notification.show("Click a row for details.");
            }
            addFilters();
        });
    }

    private void addEditColumns(GridPro<Employee> grid) {

        firstNameColumn = grid.addEditColumn(Employee::getFirstName, "firstName") // get text from, sort field
                .text(Employee::setFirstName) // where to put new text
                .setHeader(firstNameHeader); // visible header text
        emailColumn = grid.addEditColumn(Employee::getEmail, "email")
                .text(Employee::setEmail)
                .setHeader(emailHeader);

        //  Department
        departmentColumn = grid.addEditColumn(employee -> employee.getDepartment().getDepartmentName(), "employee.department.departmentName")
                .select((department, departmentName) -> {
                    final Optional<Department> optional = departmentRepository.findByDepartmentName(departmentName);
                    optional.ifPresent(department::setDepartment);
                }, departmentRepository.findAll().stream()
                        .map(Department::getDepartmentName)
                        .collect(Collectors.toList()))
                .setHeader(departmentHeader);
    }

    private HeaderRow addFilters() {
        HeaderRow filterRow = grid.appendHeaderRow();

        // First filter
        TextField firstNameField = new TextField();
        firstNameField.addValueChangeListener(event -> dataProvider.addFilter(
                person -> StringUtils.containsIgnoreCase(person.getFirstName(),
                        firstNameField.getValue())));

        firstNameField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(firstNameColumn).setComponent(firstNameField);
//        firstNameField.setSizeFull();
        firstNameField.setPlaceholder("Filter");

        // Second filter
        TextField emailField = new TextField();
        emailField.addValueChangeListener(event -> dataProvider
                .addFilter(person -> StringUtils.containsIgnoreCase(
                        String.valueOf(person.getEmail()), emailField.getValue())));

        emailField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(emailColumn).setComponent(emailField);
//        emailField.setSizeFull();
        emailField.setPlaceholder("Filter");

        // Third filter
        TextField departmentField = new TextField();
        departmentField.addValueChangeListener(event -> dataProvider
                .addFilter(person -> StringUtils.containsIgnoreCase(
                        person.getDepartment().getDepartmentName(), departmentField.getValue())));

        departmentField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(departmentColumn).setComponent(departmentField);
//        departmentField.setSizeFull();
        departmentField.setPlaceholder("Filter");
        return filterRow;
    }


//    private void refreshGrid() {
//        grid.select(null);
//        grid.getDataProvider().refreshAll();
//    }
//
//    private void clearForm() {
//        populateForm(null);
//    }
//
//    private void populateForm(Employee employee) {
//        this.employee = employee;
//        binder.readBean(this.employee); // do not like this
//    }



}
