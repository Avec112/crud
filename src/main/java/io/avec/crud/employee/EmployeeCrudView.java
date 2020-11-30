package io.avec.crud.employee;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.avec.crud.department.Department;
import io.avec.crud.department.DepartmentRepository;
import io.avec.crud.main.MainView;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Route(value = "employee-crud", layout = MainView.class)
@PageTitle("Employee Crud")
@CssImport("./styles/views/employee/employee-view.css")
//@RouteAlias(value = "", layout = MainView.class)
public class EmployeeCrudView extends Div {


    private final DepartmentRepository departmentRepository;

    /*
     Best combination is
     - setColumns
     - CrudServiceDataProvider

     Will not get filtering, but sorting will work and you get control over column ordering.
     */
    public EmployeeCrudView(EmployeeCrudService service, DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
        setId("employee-view");
        add(new Html("<p><b>Note!<b/> This does not work as intended.</p>"));
        add(new Paragraph(new Anchor("https://github.com/vaadin/vaadin-flow-components/issues/466", "Vaadin issue #466")));

//        CrudGrid<Employee> crudGrid = new CrudGrid<>(Employee.class, true);
//        Crud<Employee> crud = new Crud<>(Employee.class, crudGrid, createPersonEditor()); // provide grid
        Crud<Employee> crud = new Crud<>(Employee.class, createPersonEditor()); // receives internal grid

        // Column ordering (will loose filter)
        // todo try with and without
        crud.getGrid().setColumns("id", "firstName", "email", "department.departmentName"); // filter/sort will not work, but i get column ordering
//        crud.getGrid().setColumns("id", "firstName", "email", "department"); // filter/sort will not work, but i get column ordering

        // provider
        // todo try both to se difference with and without setColumns
//        final EmployeeDataProvider provider = new EmployeeDataProvider(service.getRepository()); // todo sizeChangeListener??? Together with setColumns sorting department will give exception
        final CrudServiceDataProvider<Employee, CrudFilter> provider = new CrudServiceDataProvider<>(service); // todo filtering does not work! with or without setColumns
        crud.setDataProvider(provider);

        crud.getGrid().removeColumnByKey("id"); // hide column

        // events
        crud.addSaveListener(e -> service.update(e.getItem()));
//        crud.addDeleteListener(e -> service.delete(e.getItem().getId()));

        // looks
        crud.addThemeVariants(CrudVariant.NO_BORDER);

        // add to layout
        add(crud);
    }

    private CrudEditor<Employee> createPersonEditor() {

        FormLayout formLayout = new FormLayout();
        TextField firstName = new TextField("First name");
        EmailField email = new EmailField("Email");
        ComboBox<String> departmentComboBox = new ComboBox<>("Department");
//        departmentComboBox.setDataProvider(new ListDataProvider<>(departmentRepository.findAll()));
        departmentComboBox.setItems(departmentRepository.findAll().stream()
                .map(Department::getDepartmentName) // to string
                .collect(Collectors.toList())); // List<String>

        formLayout.add(firstName, email, departmentComboBox);

        Binder<Employee> binder = new Binder<>();
        binder.forField(firstName)
                .asRequired(new StringLengthValidator("Please provide a name with at least two characters.", 2, 30))
//                .withValidator(new StringLengthValidator("Please provide a name with at least two characters.", 2, 30))
                .bind(Employee::getFirstName, Employee::setFirstName);
        binder.forField(email)
                .asRequired(new EmailValidator("Please provide a valid Email address."))
//                .withValidator(new EmailValidator("Please provide a valid Email address."))
                .bind(Employee::getEmail, Employee::setEmail);
        binder.forField(departmentComboBox)
                .asRequired("Please select a department")
                .withConverter(new StringToDepartmentConverter("Please select a department"))
                .bind(Employee::getDepartment, Employee::setDepartment);
//        binder.forField(departmentComboBox)
////                .withValidator(new StringLengthValidator("Please select a department", 1,40))
//                .asRequired("Please select a department")
//                .bind(employee -> (employee.getDepartment() != null) ?
//                                employee.getDepartment().getDepartmentName() : null, // null or ""
//                (employee, selected) -> {
//                    final Optional<Department> department = departmentRepository.findByDepartmentName(selected);
//                    department.ifPresent(employee::setDepartment);
//                });
        return new BinderCrudEditor<>(binder, formLayout);
    }

    /**
     * Convert between String and Department
     */
    private class StringToDepartmentConverter implements Converter<String, Department> {

        private final String errorMessage;

        public StringToDepartmentConverter(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        /**
         * Maps string to department for
         * @param value String representing a department
         * @param context not used
         * @return Result that is either ok or error
         */
        @Override
        public Result<Department> convertToModel(String value, ValueContext context) {
            final Optional<Department> byDepartmentName = departmentRepository.findByDepartmentName(value);
            return byDepartmentName.map(Result::ok).orElseGet(() -> Result.error(errorMessage));
        }

        /**
         *
         * @param value Department or null
         * @param context not used
         * @return departmentName as string if Department was provided. Otherwise null.
         */
        @Override
        public String convertToPresentation(Department value, ValueContext context) {
            if(value != null) {
                return value.getDepartmentName();
            }
            return null;
        }
    }
    
    // testing to se any difference compared to just using CrudServiceDataProvider
    public static class EmployeeDataProvider extends AbstractBackEndDataProvider<Employee, CrudFilter> {

        // A real app should hook up something like JPA
//        final List<Employee> DATABASE;
        private final EmployeeRepository repository;

        private Consumer<Long> sizeChangeListener;

        public EmployeeDataProvider(EmployeeRepository repository) {
            this.repository = repository;
        }

        @Override
        protected Stream<Employee> fetchFromBackEnd(Query<Employee, CrudFilter> query) {
            int offset = query.getOffset();
            int limit = query.getLimit();

            Stream<Employee> stream = repository.findAll().stream();

            if (query.getFilter().isPresent()) {
                stream = stream
                        .filter(predicate(query.getFilter().get()))
                        .sorted(comparator(query.getFilter().get()));
            }

            return stream.skip(offset).limit(limit);
        }

        @Override
        protected int sizeInBackEnd(Query<Employee, CrudFilter> query) {
            // For RDBMS just execute a SELECT COUNT(*) ... WHERE query
            long count = fetchFromBackEnd(query).count();

            if (sizeChangeListener != null) {
                sizeChangeListener.accept(count);
            }

            return (int) count;
        }

        private static Predicate<Employee> predicate(CrudFilter filter) {
            // For RDBMS just generate a WHERE clause
            return filter.getConstraints().entrySet().stream()
                    .map(constraint -> (Predicate<Employee>) employee -> {
                        try {
                            Object value = valueOf(constraint.getKey(), employee);
                            return value != null && value.toString().toLowerCase()
                                    .contains(constraint.getValue().toLowerCase());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    })
                    .reduce(Predicate::and)
                    .orElse(e -> true);
        }

        private static Comparator<Employee> comparator(CrudFilter filter) {
            // For RDBMS just generate an ORDER BY clause
            return filter.getSortOrders().entrySet().stream()
                    .map(sortClause -> {
                        try {
                            Comparator<Employee> comparator
                                    = Comparator.comparing(employee ->
                                    (Comparable) valueOf(sortClause.getKey(), employee));

                            if (sortClause.getValue() == SortDirection.DESCENDING) {
                                comparator = comparator.reversed();
                            }

                            return comparator;
                        } catch (Exception ex) {
                            return (Comparator<Employee>) (o1, o2) -> 0;
                        }
                    })
                    .reduce(Comparator::thenComparing)
                    .orElse((o1, o2) -> 0);
        }

        private static Object valueOf(String fieldName, Employee employee) {
            try {
                Field field = Employee.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(employee);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        void setSizeChangeListener(Consumer<Long> listener) {
            sizeChangeListener = listener;
        }

//        void persist(Employee item) {
//            if (item.getId() == null) {
//                item.setId(repository.findAll()
//                        .stream()
//                        .map(Employee::getId)
//                        .max(naturalOrder())
//                        .orElse(0) + 1);
//            }
//
//            final Optional<Employee> existingItem = find(item.getId());
//            if (existingItem.isPresent()) {
//                int position = DATABASE.indexOf(existingItem.get());
//                DATABASE.remove(existingItem.get());
//                DATABASE.add(position, item);
//            } else {
//                DATABASE.add(item);
//            }
//        }
//
//        Optional<Employee> find(Integer id) {
//            return DATABASE
//                    .stream()
//                    .filter(entity -> entity.getId().equals(id))
//                    .findFirst();
//        }
//
//        void delete(Employee item) {
//            DATABASE.removeIf(entity -> entity.getId().equals(item.getId()));
//        }
    }

}
