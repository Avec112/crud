package io.avec.crud.views.person;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.avec.crud.data.department.Department;
import io.avec.crud.data.department.DepartmentRepository;
import io.avec.crud.data.person.Person;
import io.avec.crud.data.person.PersonRepository;
import io.avec.crud.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;

@Route(value = "person", layout = MainView.class)
@PageTitle("Person")
@CssImport("./styles/views/person/person-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class PersonView extends Div {

    public PersonView(PersonRepository personRepository, DepartmentRepository departmentRepository) {
        setId("person-view");
        //add(new Label("Content placeholder"));

        GridCrud<Person> crud = new GridCrud<>(Person.class);

        // additional components
        TextField filter = new TextField();
        filter.setPlaceholder("Filter by name");
        filter.setClearButtonVisible(true);
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
                () -> personRepository.findByFirstNameContainingIgnoreCase(filter.getValue()),
                personRepository::save,
                personRepository::save,
                personRepository::delete
        );

        filter.addValueChangeListener(e -> crud.refreshGrid());
    }

}
