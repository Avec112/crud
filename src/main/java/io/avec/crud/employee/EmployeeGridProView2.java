package io.avec.crud.employee;

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
import com.vaadin.flow.data.value.ValueChangeMode;
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


    public EmployeeGridProView2(EmployeeCrudService service, DepartmentRepository departmentRepository) {
        setId("employee-view");

        Paragraph paragraph = new Paragraph("You can edit inside every cell. Database will be updated automatically.");

        GridPro<Employee> grid = new GridPro<>();
        grid.getStyle().set("position", "fixed"); // again why?
//        grid.setMultiSort(true); // multi sort columns

        VerticalLayout layout = new VerticalLayout(paragraph, grid);
        layout.setSizeFull();
        add(layout);

        ListDataProvider<Employee> dataProvider = new ListDataProvider<>(service.getRepository().findAll());
        grid.setDataProvider(dataProvider);


        Grid.Column<Employee> firstNameColumn = grid.addEditColumn(Employee::getFirstName, "firstName") // get text from, sort field
                .text(Employee::setFirstName) // where to put new text
                .setHeader("First name"); // visible header text
        Grid.Column<Employee> emailColumn = grid.addEditColumn(Employee::getEmail, "email")
                .text(Employee::setEmail)
                .setHeader("Email");

        //  Department
        Grid.Column<Employee> departmentColumn = grid.addEditColumn(employee -> employee.getDepartment().getDepartmentName(), "employee.department.departmentName")
                .select((department, departmentName) -> {
                    final Optional<Department> optional = departmentRepository.findByDepartmentName(departmentName);
                    optional.ifPresent(department::setDepartment);
                }, departmentRepository.findAll().stream()
                        .map(Department::getDepartmentName)
                        .collect(Collectors.toList()))
                .setHeader("Department");


        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        grid.setWidthFull();

        grid.setDetailsVisibleOnClick(true);

        grid.addItemPropertyChangedListener(e -> {
            service.update(e.getItem());
            Notification.show("Change stored.", 3000, Notification.Position.TOP_CENTER);
        });


        addFilters(grid, dataProvider, firstNameColumn, emailColumn, departmentColumn);


    }

    private void addFilters(GridPro<Employee> grid, ListDataProvider<Employee> dataProvider,
                            Grid.Column<Employee> firstNameColumn,
                            Grid.Column<Employee> emailColumn,
                            Grid.Column<Employee> departmentColumn) {
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
