package com.bobocode.dao;

import com.bobocode.exception.DaoOperationException;
import com.bobocode.model.Product;
import com.bobocode.util.ExerciseNotCompletedException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class ProductDaoImpl implements ProductDao {

    private final DataSource dataSource;

    public ProductDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Product product) {
        String query = "INSERT INTO products(name, producer, price, expiration_date) values('%s', '%s', %s, '%s')";
       try {
           Connection connection = dataSource.getConnection();
           PreparedStatement insert = connection.prepareStatement(String.format(query,
                   product.getName(), product.getProducer(), product.getPrice().toString().replace(',', '.'),
                   product.getExpirationDate().toString()));
           insert.execute();
           PreparedStatement select = connection.prepareStatement("SELECT * FROM products");
           ResultSet resultSet = select.executeQuery();
           resultSet.last();
       product.setId(resultSet.getLong(1));
       }catch(Exception sqlE){
           throw new DaoOperationException("Error saving product: " + product.toString());
       }
    }

    @Override
    public List<Product> findAll(){
       List<Product> result = new ArrayList<>();
       try{
           Connection connection = dataSource.getConnection();
           PreparedStatement statement = connection.prepareStatement("SELECT * FROM products");
           ResultSet resultSet = statement.executeQuery();
           while(resultSet.next()){
               Product product =new Product();
               product.setId(resultSet.getLong(1));
               product.setName(resultSet.getString(2));
               product.setProducer(resultSet.getString(3));
               product.setPrice(resultSet.getBigDecimal(4));
               product.setExpirationDate(resultSet.getObject(5, LocalDate.class));
               product.setCreationTime(resultSet.getObject(6, LocalDateTime.class));

               result.add(product);

           }
       }catch(Throwable sqlE){
           throw new DaoOperationException(sqlE.getMessage());
       }
       return result;
    }

    @Override
    public Product findOne(Long id) {

            Product result = null;
            try {
        List<Product> products = findAll();
        for (Product el : products) {
            if (id.equals(el.getId())) {
                result = el;
            }
        }
        if(result == null){
            throw new Exception();
        }
    }catch(Throwable e){
            throw new DaoOperationException("Product: " + id + "not found");
        }
        return result;
    }

    @Override
    public void update(Product product) {
        Long id = product.getId();
        String name = product.getName();
        String producer = product.getProducer();
        BigDecimal price = product.getPrice();
        LocalDate expirationDate = product.getExpirationDate();
        LocalDateTime creationTime = product.getCreationTime();

        String query = "UPDATE products " +
                "SET id = %d, name = '%s', producer = '%s', price = %s, expiration_date = '%s'" +
                " WHERE id = %d";
        try {
            if(id == null){
                throw new IllegalArgumentException();
            }
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(String.format(
                   query, id, name, producer, price.toString().replace(',', '.'), expirationDate.toString(), id));
            statement.execute();
        }catch(Throwable sqlE){
            throw new DaoOperationException("Error updating product: " + product.toString());
        }
    }

    @Override
    public void remove(Product product) {
        try{
        if(product.getId()==null){
            throw new IllegalArgumentException();
        }
     String query = "DELETE FROM products WHERE id = " + product.getId();

           Connection connection = dataSource.getConnection();
           PreparedStatement statement = connection.prepareStatement(query);
           statement.execute();
       }catch(Throwable sqlE){
           throw new DaoOperationException("Error removing product: " + product.toString());
       }
    }

}
