import java.util.Scanner;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.JsonReader;
import javax.json.JsonArray;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class Main {
    private static ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("####################################################");
        System.out.println("                Cadastro de Usuários                    ");
        System.out.println("####################################################");
        System.out.println("1 - Cadastrar novo usuário");
        System.out.println("2 - Mostrar Usuários");
        System.out.println("3 - Salvar e Sair");
        System.out.println("####################################################");

        String nome;
        String email;
        int opcao;
        do {
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine(); 

            switch (opcao) {
                case 1:
                    System.out.println("Digite o nome do Usuario: ");
                    nome = sc.nextLine();
                    System.out.println("Digite o Email do Usuário: ");
                    email = sc.nextLine();

                    Usuario user = new Usuario(nome, email);
                    usuarios.add(user);
                    user.SalvarDados();
                    break;

                case 2:
                    // Ler e exibir os dados do arquivo JSON
                    try (FileReader reader = new FileReader("usuarios.json");
                         JsonReader jsonReader = Json.createReader(reader)) {

                        JsonArray jsonArray = jsonReader.readArray();
                        System.out.println("Usuários cadastrados:");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jsonObject = jsonArray.getJsonObject(i);
                            System.out.println("Nome: " + jsonObject.getString("nome"));
                            System.out.println("Email: " + jsonObject.getString("email"));
                            System.out.println("-------------------------");
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao ler o arquivo JSON: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("Saindo do programa...");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    break;
            }
        } while (opcao != 3);

        // Salvar os usuários no arquivo JSON usando javax.json
        try {
            File file = new File("usuarios.json");
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

            // Se o arquivo já existir, leia o conteúdo existente
            if (file.exists()) {
                try (FileReader reader = new FileReader(file);
                     JsonReader jsonReader = Json.createReader(reader)) {
                    JsonArray existingArray = jsonReader.readArray();
                    for (int i = 0; i < existingArray.size(); i++) {
                        jsonArrayBuilder.add(existingArray.getJsonObject(i));
                    }
                }
            }

            // Adicione os novos usuários ao array
            for (Usuario usuario : usuarios) {
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder()
                        .add("nome", usuario.getNome())
                        .add("email", usuario.getEmail());
                jsonArrayBuilder.add(jsonObjectBuilder);
            }

            // Escreva o array atualizado de volta no arquivo
            try (FileWriter writer = new FileWriter(file);
                 JsonWriter jsonWriter = Json.createWriter(writer)) {
                jsonWriter.writeArray(jsonArrayBuilder.build());
                System.out.println("Usuários salvos em usuarios.json com sucesso!");
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar os usuários: " + e.getMessage());
        }

        sc.close();
    }

    public interface Cadastro {
        void SalvarDados();
        void MostrarDados();
    }

    public static abstract class Pessoa {
        protected String nome;
        protected String email;

        public Pessoa(String nome, String email) {
            this.nome = nome;
            this.email = email;
        }
    }

    public static class Usuario extends Pessoa implements Cadastro {

        public Usuario(String nome, String email) {
            super(nome, email);
        }

        @Override
        public void SalvarDados() {
            System.out.println("Usuário " + nome + " salvo com sucesso!");
        }

        @Override
        public void MostrarDados() {
            System.out.println("Nome: " + nome);
            System.out.println("Email: " + email);
        }

        public String getNome() {
            return nome;
        }

        public String getEmail() {
            return email;
        }
    }
}
