import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class InventoryLogic {
    
    private ArrayList<ProductModel> productList = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    public void register()
	{
		System.out.println("Enter username: ");
		String username = sc.next();

		System.out.println("Enter password: ");
		String password = sc.next();

        try (PrintWriter writer = new PrintWriter(new FileWriter("D:\\user_data.txt", true))) {
            
            writer.println(username + "," + password);
            System.out.println("Registration successful");
            System.out.println("Please Login to Continue: ");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
		
	}
	public void login() {
	    System.out.println("Enter username: ");
	    String username = sc.next();

	    System.out.println("Enter password: ");
	    String password = sc.next();

	    try (Scanner fileScanner = new Scanner(new FileInputStream("D:\\user_data.txt"))) {
	        boolean found = false;
	        while (fileScanner.hasNextLine()) {
	            String line = fileScanner.nextLine();
	            String[] data = line.split(",");
	            if (data[0].equals(username) && data[1].equals(password)) {
	                found = true;
	                break;
	            }
	        }
	        if (found) {
	            System.out.println("Login successful");
	        } else {
	            System.out.println("Invalid username or password");
	        }
	    } catch (FileNotFoundException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	  
	}
    
    public void addProduct() {
        String reply="";
        do {
            ProductModel obj = new ProductModel();
            System.out.println("Enter Product ID: ");
            int id = sc.nextInt();
            obj.setProductId(id);
            
            System.out.println("Enter Product Name: ");
            obj.setProductName(sc.next());
            
            System.out.println("Enter Product Quantity: ");
            obj.setQuantity(sc.nextInt());
            
            System.out.println("Enter Product Price: ");
            obj.setPrice(sc.nextDouble());
            
            productList.add(obj);
            
            System.out.println("Want to add more products? (Y/N) ");
            reply = sc.next();
        } while(reply.equalsIgnoreCase("y"));
    }

    public void displayAllProducts() {
        for(ProductModel obj: productList) {
            System.out.println("Product ID: " + obj.getProductId());
            System.out.println("Product Name: " + obj.getProductName());
            System.out.println("Product Quantity: " + obj.getQuantity());
            System.out.println("Product Price: " + obj.getPrice());
            System.out.println();
        }
    }
    
    public void deleteProduct() {
        System.out.println("Enter Product ID to delete: ");
        int id = sc.nextInt();
        ProductModel tempObj = null;
        
        for(ProductModel product: productList) {
            if (product.getProductId() == id) {
                tempObj = product;
                break;
            }
        }
        
        if(tempObj != null) {
            productList.remove(tempObj);
            System.out.println("Product Deleted Successfully.");
        } else {
            System.out.println("Invalid Product ID.");
        }
    }
    
    public void searchProductById() {
        System.out.println("Enter Product ID to search: ");
        int id = sc.nextInt();
        ProductModel tempObj = null;
        
        for(ProductModel product: productList) {
            if (product.getProductId() == id) {
                tempObj = product;
                break;
            }
        }
        
        if(tempObj != null) {
            System.out.println("Product ID: " + tempObj.getProductId());
            System.out.println("Product Name: " + tempObj.getProductName());
            System.out.println("Product Quantity: " + tempObj.getQuantity());
            System.out.println("Product Price: " + tempObj.getPrice());
        } else {
            System.out.println("Product not found.");
        }
    }
    
    public void updateProductDetailsById() {
        System.out.println("Enter Product ID to update: ");
        int id = sc.nextInt();
        ProductModel tempObj = null;
        
        for(ProductModel product: productList) {
            if (product.getProductId() == id) {
                tempObj = product;
                break;
            }
        }
        
        if(tempObj != null) {
            System.out.println("Enter New Product Name: ");
            tempObj.setProductName(sc.next());
            
            System.out.println("Enter New Product Quantity: ");
            tempObj.setQuantity(sc.nextInt());
            
            System.out.println("Enter New Product Price: ");
            tempObj.setPrice(sc.nextDouble());
            
            System.out.println("Product details updated successfully.");
        } else {
            System.out.println("Invalid Product ID.");
        }
    }
    
    public void generateReport() {
        System.out.println("Inventory Report");
        System.out.println("----------------");
        System.out.println("Product\t\tQuantity");

        for (ProductModel product : productList) {
            System.out.println(product.getProductName() + "\t\t" + product.getQuantity());
        }
    }
    
   public void saleOrderManagement() {
        System.out.println("Enter Product ID for sale: ");
        int id = sc.nextInt();
        ProductModel product = findProductById(id);
        if (product != null) {
            System.out.println("Enter quantity to sell: ");
            int quantityToSell = sc.nextInt();
            if (quantityToSell <= product.getQuantity()) {
                product.setQuantity(product.getQuantity() - quantityToSell);
                System.out.println("Sale successful. Remaining stock: " + product.getQuantity());
                logTransaction("Sale", id, quantityToSell);
            } else {
                System.out.println("Insufficient stock. Available quantity: " + product.getQuantity());
            }
        } else {
            System.out.println("Product not found.");
        }
    }

    public void purchaseOrder() {
        System.out.println("Enter Product ID for restocking: ");
        int id = sc.nextInt();
        ProductModel product = findProductById(id);
        int quantityToAdd = 0;

        if (product != null) {
            System.out.println("Enter quantity to add: ");
            quantityToAdd = sc.nextInt();
            product.setQuantity(product.getQuantity() + quantityToAdd);
            System.out.println("Restock successful. New stock: " + product.getQuantity());
            logTransaction("Purchase", id, quantityToAdd);
        } else {
            System.out.println("Product not found. Please add the product first.");
        }
    }

    private ProductModel findProductById(int productId) {
        for (ProductModel product : productList) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }
    
    public void writeArrayListToFile() {
        try {
            FileOutputStream fout = new FileOutputStream("productList.ser");
            ObjectOutputStream oout = new ObjectOutputStream(fout);
            oout.writeObject(productList);
            System.out.println("Data saved to file.");
            oout.close();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void readArrayListFromFile() {
        try {
            FileInputStream fin = new FileInputStream("productList.ser");
            ObjectInputStream oin = new ObjectInputStream(fin);
            productList = (ArrayList<ProductModel>) oin.readObject();
            oin.close();
            fin.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Starting with an empty inventory.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
