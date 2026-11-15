import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.Duration;

public class Principal {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Cliente> clientes = new ArrayList<>();
    static ArrayList<Veiculo> veiculos = new ArrayList<>();
    static ArrayList<Permanencia> historico = new ArrayList<>();

    public static void main(String[] args) {

        int opcao;

        do {
            System.out.println("\n=== ESTACIONAMENTO ===");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Cadastrar veiculo");
            System.out.println("3 - Registrar entrada");
            System.out.println("4 - Registrar saida");
            System.out.println("5 - Ver historico");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {

                case 1:
                    cadastrarCliente();
                    break;

                case 2:
                    cadastrarVeiculo();
                    break;

                case 3:
                    registrarEntrada();
                    break;

                case 4:
                    registrarSaida();
                    break;

                case 5:
                    consultarHistorico();
                    break;

                case 0:
                    System.out.println("Saindo do sistema...");
                    break;

                default:
                    System.out.println("Opcao invalida!");
            }

        } while (opcao != 0);
    }

    static void cadastrarCliente() {

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Telefone: ");
        String telefone = sc.nextLine();

        clientes.add(new Cliente(nome, telefone));

        System.out.println("Cliente cadastrado!");
    }

    static void cadastrarVeiculo() {

        System.out.print("Placa: ");
        String placa = sc.nextLine();

        System.out.print("Modelo: ");
        String modelo = sc.nextLine();

        System.out.println("Clientes:");

        for (int i = 0; i < clientes.size(); i++) {
            System.out.println(i + " - " + clientes.get(i).nome);
        }

        if (clientes.isEmpty()) {
            System.out.println("Cadastre um cliente primeiro!");
            return;
        }

        System.out.print("Numero do cliente: ");
        int numero = Integer.parseInt(sc.nextLine());

        if (numero < 0 || numero >= clientes.size()) {
            System.out.println("Cliente invalido!");
            return;
        }

        Veiculo v = new Veiculo(placa, modelo, clientes.get(numero));

        veiculos.add(v);

        System.out.println("Veiculo cadastrado!");
    }

    static void registrarEntrada() {

        System.out.print("Placa do veiculo: ");
        String placa = sc.nextLine();

        for (Veiculo v : veiculos) {

            if (v.placa.equalsIgnoreCase(placa)) {

                for (Permanencia p : historico) {
                    if (p.veiculo == v && p.saida == null) {
                        System.out.println("Veiculo ja esta estacionado!");
                        return;
                    }
                }

                historico.add(new Permanencia(v));

                System.out.println("Entrada registrada!");
                return;
            }
        }

        System.out.println("Veiculo nao encontrado!");
    }

    static void registrarSaida() {

        System.out.print("Placa do veiculo: ");
        String placa = sc.nextLine();

        for (Permanencia p : historico) {

            if (p.veiculo.placa.equalsIgnoreCase(placa)
                    && p.saida == null) {

                p.saida = LocalDateTime.now();

                long minutos = Duration.between(
                    p.entrada, p.saida
                ).toMinutes();

                if (minutos <= 15) {
                    p.valor = 0;
                } else {
                    long horas = (long) Math.ceil(
                        (minutos - 15) / 60.0
                    );

                    p.valor = horas * 5;
                }

                System.out.println("Saida registrada!");
                System.out.println("Tempo: " + minutos + " minutos");

                System.out.printf("Valor: R$ %.2f%n", p.valor);

                return;
            }
        }

        System.out.println("Veiculo nao encontrado ou sem entrada!");
    }

    static void consultarHistorico() {

        System.out.println("\n=== HISTORICO ===");

        for (Permanencia p : historico) {

            System.out.println("Placa: " + p.veiculo.placa);
            System.out.println("Modelo: " + p.veiculo.modelo);
            System.out.println("Entrada: " + p.entrada);
            System.out.println("Saida: " + p.saida);
            System.out.printf("Valor: R$ %.2f%n", p.valor);

            System.out.println("------------------");
        }
    }

    static class Cliente {

        String nome;
        String telefone;

        Cliente(String nome, String telefone) {
            this.nome = nome;
            this.telefone = telefone;
        }
    }

    static class Veiculo {

        String placa;
        String modelo;
        Cliente cliente;

        Veiculo(String placa, String modelo, Cliente cliente) {
            this.placa = placa;
            this.modelo = modelo;
            this.cliente = cliente;
        }
    }

    static class Permanencia {

        Veiculo veiculo;
        LocalDateTime entrada;
        LocalDateTime saida;
        double valor;

        Permanencia(Veiculo veiculo) {
            this.veiculo = veiculo;
            this.entrada = LocalDateTime.now();
            this.saida = null;
            this.valor = 0;
        }
    }
}