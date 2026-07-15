/**
 * DisplayEmployeeListAndEditEmployeeFormUsingRESTfulWebService.java
 *
 * Main / driver class for Exercise 1.
 *
 * Problem Statement:
 * "Display Employee List and Edit Employee form using RESTful Web
 *  Service" - the Angular screen that previously showed hard coded
 *  employee data must now get its data from a RESTful Web Service.
 *  Clicking Edit on an employee should show the Edit Employee form
 *  populated with values retrieved from the service.
 *
 * This driver wires together:
 *   EmployeeRestClient (simulated RESTful Web Service)
 *   EmployeeListView   (Employee List screen)
 *   EditEmployeeForm   (Edit Employee screen)
 *
 * Run with:  java DisplayEmployeeListAndEditEmployeeFormUsingRESTfulWebService
 */
public class DisplayEmployeeListAndEditEmployeeFormUsingRESTfulWebService {

    public static void main(String[] args) {

        // The "server side" RESTful Web Service (simulated in Core Java)
        EmployeeRestClient restClient = new EmployeeRestClient();

        // The two UI screens, both consuming the REST client
        EmployeeListView employeeListView = new EmployeeListView(restClient);
        EditEmployeeForm editEmployeeForm = new EditEmployeeForm(restClient);

        // Step 1: Display the Employee List screen (GET /employees)
        employeeListView.render();

        // Step 2: Simulate clicking "Edit" on employee id 102 (GET /employees/102)
        Employee employeeBeingEdited = editEmployeeForm.load(102);

        // Step 3: Simulate editing the department field and saving (PUT /employees/102)
        if (employeeBeingEdited != null) {
            editEmployeeForm.save(employeeBeingEdited, "Human Resources");
        }

        // Step 4: Re-render the Employee List screen to show the updated data
        System.out.println("\nRe-loading the Employee List screen after the edit:");
        employeeListView.render();
    }
}
