package io.avec.crud.employee;

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

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "employee-grid", layout = MainView.class)
@PageTitle("Employee GridPro")
@CssImport("./styles/views/employee/employee-view.css")
//@RouteAlias(value = "", layout = MainView.class)
public class EmployeeGridProView extends Div {





    public EmployeeGridProView(EmployeeCrudService service, DepartmentRepository departmentRepository) {
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


        grid.addEditColumn(Employee::getFirstName, "firstName") // get text from, sort field
                .text(Employee::setFirstName) // where to put new text
                .setHeader("First name"); // visible header text
        grid.addEditColumn(Employee::getEmail, "email")
                .text(Employee::setEmail)
                .setHeader("Email");

        //  Department
        grid
                .addEditColumn(employee -> employee.getDepartment().getDepartmentName(), "employee.department.departmentName")
                .select((department, departmentName) -> {
                    final Optional<Department> optional = departmentRepository.findByDepartmentName(departmentName);
                    optional.ifPresent(department::setDepartment);
                }, departmentRepository.findAll().stream()
                        .map(Department::getDepartmentName)
                        .collect(Collectors.toList()))
                .setHeader("Department");

//        grid.addColumn(employee -> employee.getDepartment().getDepartmentName()).setHeader("Department name");

        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        grid.setWidthFull();

        grid.setDetailsVisibleOnClick(true);

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
