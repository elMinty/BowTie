module project.bowtie {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens project.bowtie to javafx.fxml;
    exports project.bowtie;
    exports project.bowtie.App.Controllers;
    opens project.bowtie.App.Controllers to javafx.fxml;
}