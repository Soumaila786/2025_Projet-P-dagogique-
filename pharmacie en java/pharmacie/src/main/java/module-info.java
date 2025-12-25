module com.projet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires com.github.librepdf.openpdf;

    opens com.projet to javafx.fxml;
    opens com.projet.controllers to javafx.fxml;
    opens com.projet.models to javafx.fxml;
    opens com.projet.services to javafx.fxml;
    exports com.projet;
}
