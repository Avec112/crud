package io.avec.crud.employee;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.avec.crud.department.Department;
import io.avec.crud.department.DepartmentRepository;
import io.avec.crud.main.MainView;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "employee", layout = MainView.class)
@PageTitle("Employee")
@CssImport("./styles/views/employee/employee-view.css")
//@RouteAlias(value = "", layout = MainView.class)
public class EmployeeView extends Div {



//    public EmployeeView(EmployeeCrudService service) {
//        setId("employee-view");
//
//        Crud<Employee> crud = new Crud<>(Employee.class, createPersonEditor());
//
//
//        crud.setDataProvider(new CrudServiceDataProvider<Employee,Void>(service));
//        crud.addSaveListener(e -> service.update(e.getItem()));
//        crud.addDeleteListener(e -> service.delete(e.getItem().getId()));
//        crud.getGrid().setColumns("id", "firstName", "email", "department.departmentName");
//        crud.getGrid().removeColumnByKey("id");
//        crud.addThemeVariants(CrudVariant.NO_BORDER);
//        add(crud);
//    }

//    private CrudEditor<Employee> createPersonEditor() {
//
//        FormLayout formLayout = new FormLayout();
//        TextField firstName = new TextField("First name");
//        TextField email = new TextField("Email");
//        ComboBox<Department> departmentComboBox = new ComboBox<>("Department");
//        formLayout.add(firstName, email, departmentComboBox);
//
//        Binder<Employee> binder = new Binder<>(Employee.class);
//        binder.bind(firstName, Employee::getFirstName, Employee::setFirstName);
//        binder.bind(email, Employee::getEmail, Employee::setEmail);
//        binder.bind(departmentComboBox,Employee::getDepartment, Employee::setDepartment);
//
//
//        return new BinderCrudEditor<>(binder, formLayout);
//    }

//    private final GridPro<Employee> grid = new GridPro<>(Employee.class);

//    private final TextField firstName = new TextField("First name");
//    private final TextField email = new TextField("Email");

//    private final Button cancel = new Button("Cancel");
//    private final Button save = new Button("Save");

//    private final BeanValidationBinder<Employee> binder = new BeanValidationBinder<>(Employee.class);

//    private Employee employee;

    public EmployeeView(EmployeeCrudService service, DepartmentRepository departmentRepository) {
        setId("employee-view");

        this.getStyle().set("display", "grid"); // why??
        this.setSizeFull();

        GridPro<Employee> grid = new GridPro<>();

        VerticalLayout layout = new VerticalLayout(grid);
        layout.setSizeFull();
        add(layout);

        ListDataProvider<Employee> dataProvider = new ListDataProvider<>(service.getRepository().findAll());
        grid.setDataProvider(dataProvider);
//        grid.setDataProvider(new CrudServiceDataProvider<Employee,Void>(service));

        grid.addEditColumn(Employee::getFirstName).text(Employee::setFirstName).setHeader("First name");
        grid.addEditColumn(Employee::getEmail).text(Employee::setEmail).setHeader("Email");

        // TODO add combo for Department
        //        grid
//                .addEditColumn(employee -> employee.getDepartment().getDepartmentName())
//                .select((item, newValue) -> {
//                   service.getRepository().fi
//                    item.setDepartment(newValue);
//                }, departmentRepository.findDistinctByDepartmentName().stream().map(Department::getDepartmentName).collect(Collectors.toList()))
//                .setHeader("Department");
        grid.addColumn(employee -> employee.getDepartment().getDepartmentName()).setHeader("Department name"); // TODO editable

        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        grid.setWidthFull();

        grid.addItemPropertyChangedListener(e -> {
            service.update(e.getItem());
            Notification.show("Change stored.", 3000, Notification.Position.TOP_CENTER);
        });




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
