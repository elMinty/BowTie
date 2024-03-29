module BowTie{

    requires java.xml.bind;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.swing;

    opens BowTie to javafx.fxml;
    opens BowTie.IO to java.xml.bind;
    exports BowTie;

    exports BowTie.Controllers.ViewPane;
    opens BowTie.Controllers.ViewPane to javafx.fxml;
    exports BowTie.Controllers;
    opens BowTie.Controllers to javafx.fxml;
    exports BowTie.Controllers.ViewPane.Menus;
    opens BowTie.Controllers.ViewPane.Menus to javafx.fxml;

}