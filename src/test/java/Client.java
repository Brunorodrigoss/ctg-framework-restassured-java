public class Client {

    private String nome;
    private int idade;
    private int id;

    public Client() {
        this.nome = "N/D";
        this.idade = 0;
        this.id = 0;
    }
    public Client( String nome, int idade, int id) {
        this.nome = nome;
        this.idade = idade;
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return this.idade;
    }

    public void setIdade(int idade){
        this.idade = idade;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }




}
