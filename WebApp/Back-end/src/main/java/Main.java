import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import helper.Utility;
import models.User;
import spark.*;

import java.sql.SQLException;
import java.util.HashMap;

import static spark.Spark.*;

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

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        Main.apply();
        get("/hello", (req, res) -> "Hello Panouri World");

        setEndpoints();
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
            Endpoints.setClientEndpoints(connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
