package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.example.models.Todo;
import com.example.models.User;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

/**
 * Created by edmond on 18/12/2016.
 */
public class Endpoints {


    public static void setTodoEndpoints(ConnectionSource connectionSource) {
        try {
            Dao<Todo, String> todoDao = DaoManager.createDao(connectionSource, Todo.class);
            TableUtils.createTableIfNotExists(connectionSource, Todo.class);

            Spark.post("/todos", (request, response) -> {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Todo todo = mapper.readValue(request.body(), Todo.class);
                    if (!todo.isValid()) {
                        response.status(HTTP_BAD_REQUEST);
                        return "";
                    }
                    todoDao.create(todo);

                    response.status(200);
                    response.type("application/json");
                    return "success";
                } catch (Exception e) {
                    response.status(HTTP_BAD_REQUEST);
                    return "failure";
                }
            });

            Spark.get("/todos", (request, response) -> {
                List<Todo> todos = todoDao.queryForAll();

                response.status(200);
                response.type("application/json");
                return dataToJson(todos);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setUserEndpoints(ConnectionSource connectionSource) {

        try {
            Dao<User, String> userDao = DaoManager.createDao(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, User.class);

            Spark.post("/users", (request, response) -> {
                try {
                    String username = request.queryParams("username");
                    String email = request.queryParams("email");

                    User user = new User();
                    user.setUsername(username);
                    user.setEmail(email);

                    userDao.create(user);

                    response.status(201);
                    response.type("application/json");
                    return user;
                } catch (Exception e) {
                    return "";
                }
            });

            Spark.get("/users/:id", new Route() {
                @Override
                public Object handle(Request request, Response response) {
                    try {
                        User user = userDao.queryForId(request.params(":id"));
                        if (user != null) {
                            return "Username: " + user.getUsername(); // or JSON? :-)
                        } else {
                            response.status(404); // 404 Not found
                            return "User not found";
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return response;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e){
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }

}