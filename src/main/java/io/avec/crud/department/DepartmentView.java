package io.avec.crud.department;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.avec.crud.main.MainView;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;

@Route(value = "department", layout = MainView.class)
@PageTitle("Department")
@CssImport("./styles/views/department/department-view.css")
public class DepartmentView extends Div {

    public DepartmentView(DepartmentRepository departmentRepository) {
        setId("department-view");

        GridCrud<Department> crud = new GridCrud<>(Department.class);


        TextField filter = new TextField();
        filter.setPlaceholder("Filter by department");
        filter.setClearButtonVisible(true);
        crud.getCrudLayout().addFilterComponent(filter);

        crud.getGrid().setColumns("departmentName", "floor");
        crud.getGrid().setColumnReorderingAllowed(true);

        crud.getCrudFormFactory().setVisibleProperties(CrudOperation.ADD,"departmentName", "floor");
        crud.getCrudFormFactory().setVisibleProperties("departmentName", "floor");
//        crud.getCrudFormFactory().setFieldProvider("department", new ComboBoxProvider<>(departmentRepository.findAll()));
//        crud.getCrudFormFactory().setFieldProvider("department", new ComboBoxProvider<>("Department", departmentRepository.findAll(), new TextRenderer<>(Department::getDepartmentName), Department::getDepartmentName));

        // layout configuration
        setSizeFull();
        add(crud);

        // logic configuration
        crud.setOperations(
                () -> departmentRepository.findByDepartmentNameContainingIgnoreCase(filter.getValue()),
                departmentRepository::save,
                departmentRepository::save,
                departmentRepository::delete
        );

        filter.addValueChangeListener(e -> crud.refreshGrid());
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);
        filter.focus();
//        filter.addFocusShortcut(Key.KEY_S, KeyModifier.CONTROL, KeyModifier.SHIFT);
    }

}
