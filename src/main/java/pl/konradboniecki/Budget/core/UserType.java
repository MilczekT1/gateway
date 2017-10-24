package pl.konradboniecki.Budget.core;

public enum UserType {
    
    ADMIN("ADMIN"), SUBSCRIBER("SUBSCRIBER"), USER("USER");
    
    private String role;
    
    private UserType(String role){
        setRole(role);
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getRole() {
        return role;
    }
}
