/**
 * InventoryManagementSystem is the entry point demonstrating:
 *   1. A centralized ConfigServer (Spring Cloud Config Server stand-in).
 *   2. A ServiceRegistry (Eureka stand-in) that services register with.
 *   3. ProductService and InventoryService discovering each other through
 *      the registry instead of using hardcoded URLs.
 *
 * Running this class will:
 *   - Print the centralized configuration loaded from ConfigServer.
 *   - Start ProductService, which registers itself as "PRODUCT-SERVICE".
 *   - Start InventoryService, which registers itself as "INVENTORY-SERVICE".
 *   - Print the registry contents to prove discovery is working.
 *   - Exercise the REST endpoints of both services, including
 *     InventoryService looking up ProductService dynamically and calling it.
 */
public class InventoryManagementSystem {

    public static void main(String[] args) throws Exception {
        System.out.println("================ Centralized Configuration ================\n");
        ConfigServer configServer = ConfigServer.getInstance();
        configServer.printConfiguration();

        int productServicePort = configServer.getInt("product-service.port", 9081);
        int inventoryServicePort = configServer.getInt("inventory-service.port", 9082);

        System.out.println("\n================ Starting Microservices ================\n");

        // Start Product Service - registers itself with the ServiceRegistry
        ProductService productService = new ProductService(productServicePort);
        productService.seedData();
        productService.start();

        // Start Inventory Service - also registers itself with the ServiceRegistry
        InventoryService inventoryService = new InventoryService(inventoryServicePort);
        inventoryService.start();

        Thread.sleep(500);

        System.out.println();
        ServiceRegistry.getInstance().printRegistry();

        String inventoryBaseUrl = "http://localhost:" + inventoryServicePort;

        System.out.println("\n================ DEMO: REST client calls ================\n");

        // 1. List all products via Inventory Service (discovers Product Service internally)
        System.out.println(">> GET /inventory  (Inventory Service discovers Product Service)");
        System.out.println(HttpClientHelper.get(inventoryBaseUrl + "/inventory"));

        // 2. Check low-stock status for the 4K Monitor (id 3, seeded with only 3 units)
        System.out.println("\n>> GET /inventory/3/check  (checking low-stock threshold)");
        System.out.println(HttpClientHelper.get(inventoryBaseUrl + "/inventory/3/check"));

        // 3. Restock the 4K Monitor using the centrally configured restock quantity
        System.out.println("\n>> POST /inventory/3/restock  (restocks using ConfigServer value)");
        System.out.println(HttpClientHelper.post(inventoryBaseUrl + "/inventory/3/restock", "{}"));

        // 4. Re-check low-stock status to confirm the restock worked
        System.out.println("\n>> GET /inventory/3/check  (should no longer be low stock)");
        System.out.println(HttpClientHelper.get(inventoryBaseUrl + "/inventory/3/check"));

        System.out.println("\n================ DEMO COMPLETE ================");

        Thread.sleep(1000);
        productService.stop();
        inventoryService.stop();
        System.out.println("\nBoth microservices have been stopped and deregistered.");
    }
}
