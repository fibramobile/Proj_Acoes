package action.dev.project15;

/**
 * Constantes
 *
 * Finalizado: OK
 * Revisado: OK
 */
public class Constants {

    // Nome no backup
    public static final String nameBackup = "ACTIONS";

    // Arquivo
    public static final String fileName = "storage.json";

    // Ações
    public static final String[] actions = {
            "ABEV3", "BTOW3", "B3SA3", "BBSE3", "BRML3",
            "BBDC3", "BBDC4", "BRAP4", "BBAS3", "BRKM5",
            "BRFS3", "CCRO3", "CMIG4", "CIEL3", "CPLE6",
            "CSAN3", "CVCB3", "CYRE3", "ECOR3", "ELET3",
            "ELET6", "EMBR3", "EGIE3", "EQTL3", "ESTC3",
            "FIBR3", "FLRY3", "GOAU4", "GGBR4", "GOLL4",
            "HYPE3", "IGTA3", "ITSA4", "ITUB4", "JBSS3",
            "KLBN11", "KROT3", "RENT3", "LAME4", "LREN3",
            "MGLU3", "MRFG3", "MRVE3", "MULT3", "NATU3",
            "PCAR4", "PETR3", "PETR4", "QUAL3", "RADL3",
            "RAIL3", "SBSP3", "SANB11", "CSNA3", "SMLS3",
            "SUZB3", "TAEE11", "VIVT4", "TIMP3", "USIM5",
            "VALE3", "VVAR11", "WEGE3", "MILS3"
    };

    /* Regex data dd/mm/yyyy */
    public static final String DATE_REGEX = "^([0-2][0-9]||3[0-1])/"
            + "(0[0-9]||1[0-2])/"
            + "([0-9][0-9])?[0-9][0-9]$";

    /* Regex double */
    public static final String DOUBLE_REGEX = "^[\\d]+[\\.]?[\\d]*$";
}
