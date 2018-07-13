package pl.konradboniecki.utils;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class BudgetAdress implements Serializable {

    @Getter
    private static final String URI = "http://77.55.237.245";

    @Getter
    private static final String LOCALHOST = "http://localhost";

}
