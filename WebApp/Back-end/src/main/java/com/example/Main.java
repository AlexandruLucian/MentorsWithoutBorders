/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.sql.SQLException;
import java.util.HashMap;

import static spark.Spark.get;
import static spark.SparkBase.port;

import com.example.helper.Utility;

@Controller
@SpringBootApplication
public class Main {

  private static final HashMap<String, String> corsHeaders = new HashMap<String, String>();

  static {
    corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
    corsHeaders.put("Access-Control-Allow-Origin", "*");
    corsHeaders.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
    corsHeaders.put("Access-Control-Allow-Credentials", "true");
  }

  public final static void apply() {
    Filter filter = new Filter() {
      @Override
      public void handle(Request request, Response response) throws Exception {
        corsHeaders.forEach((key, value) -> {
          response.header(key, value);
        });
      }
    };
    Spark.after(filter);
  }

  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567;
  }

  private static void setEndpoints() {
    String databaseUrl = Utility.DB_URL;

    try {
      ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
      ((JdbcConnectionSource)connectionSource).setUsername(Utility.DB_USER);
      ((JdbcConnectionSource)connectionSource).setPassword(Utility.DB_PASSWORD);

      Endpoints.setUserEndpoints(connectionSource);
      Endpoints.setTodoEndpoints(connectionSource);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /*@Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;*/

  public static void main(String[] args) throws Exception {
    //SpringApplication.run(Main.class, args);
    port(getHerokuAssignedPort());
    Main.apply();
    get("/hello", (req, res) -> "Hello Mentors Without Borders");

    setEndpoints();
  }

  /*@RequestMapping("/")
  String index() {
    return "index";
  }

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }*/

}
