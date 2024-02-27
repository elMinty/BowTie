module project.bowtie {

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


    opens project.bowtie to javafx.fxml;
    opens project.bowtie.IO to java.xml.bind;
    exports project.bowtie;
    opens project.bowtie.App.Controllers to javafx.fxml;
    exports project.bowtie.App.Controllers.ViewPane;
    opens project.bowtie.App.Controllers.ViewPane to javafx.fxml;
    exports project.bowtie.App.Controllers;
    exports project.bowtie.App.Controllers.ViewPane.Menus;
    opens project.bowtie.App.Controllers.ViewPane.Menus to javafx.fxml;
}