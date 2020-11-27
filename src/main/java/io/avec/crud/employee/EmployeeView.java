package io.avec.crud.employee;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.crud.CrudVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.avec.crud.department.Department;
import io.avec.crud.main.MainView;
import org.vaadin.artur.helpers.CrudServiceDataProvider;


@Route(value = "employee", layout = MainView.class)
@PageTitle("Employee")
@CssImport("./styles/views/employee/employee-view.css")
//@RouteAlias(value = "", layout = MainView.class)
public class EmployeeView extends Div {


//    private TextField firstName;
//    private TextField email;
//
//    private Button cancel = new Button("Cancel");
//    private Button save = new Button("Save");
//
//    private BeanValidationBinder<Person> binder;
//
//    private Person person;

    public EmployeeView(EmployeeService service) {
        setId("employee-view");

        Crud<Employee> crud = new Crud<>(Employee.class, createPersonEditor());

        crud.setDataProvider(new CrudServiceDataProvider<Employee,Void>(service));
        crud.addSaveListener(e -> service.update(e.getItem()));
        crud.addDeleteListener(e -> service.delete(e.getItem().getId()));

//        crud.getGrid().removeColumnByKey("id");
        crud.addThemeVariants(CrudVariant.NO_BORDER);
        add(crud);

    }

//    public EmployeeView(EmployeeService service) {
//        setId("employee-view");
//
//        GridPro<Employee> grid = new GridPro<>(Employee.class);
//        grid.setColumns("firstName", "email"/*, "department.departmentName"*/);
//        grid.getColumns().forEach(column -> column.setAutoWidth(true));
//        grid.setDataProvider(new CrudServiceDataProvider<Employee,Void>(service));
//        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
//        grid.setHeightFull();
//
//        // when a row is selected or deselected, populate form
//        grid.asSingleSelect().addValueChangeListener(event -> {
//            if (event.getValue() != null) {
//                Optional<Employee> personFromBackend = service.get(event.getValue().getId());
//                // when a row is selected but the data is no longer available, refresh grid
//                if (personFromBackend.isPresent()) {
//                    populateForm(personFromBackend.get());
//                } else {
//                    refreshGrid();
//                }
//            } else {
//                clearForm();
//            }
//        });
//
//        // Configure Form
//        binder = new BeanValidationBinder<>(Person.class);
//
//        // Bind fields. This where you'd define e.g. validation rules
//        binder.bindInstanceFields(this);
//
//        cancel.addClickListener(e -> {
//            clearForm();
//            refreshGrid();
//        });
//
//        save.addClickListener(e -> {
//            try {
//                if (this.person == null) {
//                    this.person = new Person();
//                }
//                binder.writeBean(this.person);
//                personService.update(this.person);
//                clearForm();
//                refreshGrid();
//                Notification.show("Person details stored.");
//            } catch (ValidationException validationException) {
//                Notification.show("An exception happened while trying to store the person details.");
//            }
//        });
//
//    }

    private CrudEditor<Employee> createPersonEditor() {

        FormLayout formLayout = new FormLayout();
        TextField firstName = new TextField("First name");
        TextField email = new TextField("Email");
        ComboBox<Department> departmentComboBox = new ComboBox<>("Department");
        formLayout.add(firstName, email, departmentComboBox);

        Binder<Employee> binder = new Binder<>(Employee.class);
        binder.bind(firstName, Employee::getFirstName, Employee::setFirstName);
        binder.bind(email, Employee::getEmail, Employee::setEmail);
        binder.bind(departmentComboBox,Employee::getDepartment, Employee::setDepartment);


        return new BinderCrudEditor<>(binder, formLayout);
    }

}
