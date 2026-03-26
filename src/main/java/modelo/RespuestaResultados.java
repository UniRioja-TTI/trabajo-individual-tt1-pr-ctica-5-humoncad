package modelo;

public class RespuestaResultados {
    private boolean done;
    private Integer tokenSolicitud;
    private String errorMessage;
    private String data;

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
    public Integer getTokenSolicitud() { return tokenSolicitud; }
    public void setTokenSolicitud(Integer t) { this.tokenSolicitud = t; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String e) { this.errorMessage = e; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
}