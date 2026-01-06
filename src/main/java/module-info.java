module ec.edu.espoch.estudiantejavafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens ec.edu.espoch.estudiantejavafx to javafx.fxml;
    exports ec.edu.espoch.estudiantejavafx;
}
