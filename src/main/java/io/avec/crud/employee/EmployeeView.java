package io.avec.crud.employee;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.avec.crud.department.Department;
import io.avec.crud.department.DepartmentRepository;
import io.avec.crud.main.MainView;
import com.vaadin.flow.router.RouteAlias;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;

@Route(value = "employee", layout = MainView.class)
@PageTitle("Employee")
@CssImport("./styles/views/employee/employee-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class EmployeeView extends Div {

    public EmployeeView(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        setId("employee-view");

        GridCrud<Employee> crud = new GridCrud<>(Employee.class);

        // additional components
        TextField filter = new TextField();
        filter.setPlaceholder("Filter by department");
        filter.setClearButtonVisible(true);
        crud.getCrudLayout().addFilterComponent(filter);

        // grid configuration
        crud.getGrid().setColumns("firstName", "email", "department.departmentName");
        crud.getGrid().setColumnReorderingAllowed(true);

        // form configuration
//        crud.getCrudFormFactory().setUseBeanValidation(true); // requires JSR-303 Bean Validation implementation not found on the classpath
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.ADD,"firstName", "email", "department");
        crud.getCrudFormFactory().setVisibleProperties("firstName", "email", "department");
        crud.getCrudFormFactory().setFieldProvider("department", new ComboBoxProvider<>(departmentRepository.findAll()));
        crud.getCrudFormFactory().setFieldProvider("department", new ComboBoxProvider<>("Department", departmentRepository.findAll(), new TextRenderer<>(Department::getDepartmentName), Department::getDepartmentName));

        // layout configuration
        setSizeFull();
        add(crud);

        // logic configuration
        crud.setOperations(
                () -> employeeRepository.findByDepartmentDepartmentNameContainingIgnoreCase(filter.getValue()),
//                () -> employeeRepository.findByDepartmentDepartmentNameContainingIgnoreCase(filter.getValue() == null? "": filter.getValue()),
//                () -> employeeRepository.findByFirstNameContainingIgnoreCase(filter.getValue()),
                employeeRepository::save,
                employeeRepository::save,
                employeeRepository::delete
        );

        filter.addValueChangeListener(e -> crud.refreshGrid());
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);
    }

}
