package io.avec.crud.views.employee;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.avec.crud.data.department.Department;
import io.avec.crud.data.department.DepartmentRepository;
import io.avec.crud.data.employee.Employee;
import io.avec.crud.data.employee.EmployeeRepository;
import io.avec.crud.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;

import java.util.stream.Collectors;

@Route(value = "employee", layout = MainView.class)
@PageTitle("Employee")
@CssImport("./styles/views/employee/employee-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class EmployeeView extends Div {

    public EmployeeView(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        setId("employee-view");
        //add(new Label("Content placeholder"));

        GridCrud<Employee> crud = new GridCrud<>(Employee.class);

        ComboBox<String> filter = new ComboBox<>();
        filter.setDataProvider(new ListDataProvider<>(departmentRepository.findAll().stream().map(Department::getDepartmentName).collect(Collectors.toList())));
        filter.setClearButtonVisible(true);
        filter.setPlaceholder("Filter by department");

        // additional components
//        TextField filter = new TextField();
//        filter.setPlaceholder("Filter by department");
//        filter.setClearButtonVisible(true);
        crud.getCrudLayout().addFilterComponent(filter);

        // grid configuration
        crud.getGrid().setColumns("firstName", "department.departmentName");
        crud.getGrid().setColumnReorderingAllowed(true);

        // form configuration
//        crud.getCrudFormFactory().setUseBeanValidation(true); // requires JSR-303 Bean Validation implementation not found on the classpath
        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.ADD,"firstName", "department");
        crud.getCrudFormFactory().setVisibleProperties("firstName", "department");
        crud.getCrudFormFactory().setFieldProvider("department", new ComboBoxProvider<>(departmentRepository.findAll()));
        crud.getCrudFormFactory().setFieldProvider("department", new ComboBoxProvider<>("Department", departmentRepository.findAll(), new TextRenderer<>(Department::getDepartmentName), Department::getDepartmentName));

        // layout configuration
        setSizeFull();
        add(crud);

        // logic configuration
        crud.setOperations(
                () -> employeeRepository.findByDepartmentDepartmentNameContainingIgnoreCase(filter.getValue() == null? "": filter.getValue()),
//                () -> employeeRepository.findByFirstNameContainingIgnoreCase(filter.getValue()),
                employeeRepository::save,
                employeeRepository::save,
                employeeRepository::delete
        );

        filter.addValueChangeListener(e -> crud.refreshGrid());
    }

}
