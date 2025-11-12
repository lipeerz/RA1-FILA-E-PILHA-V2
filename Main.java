import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Exceção personalizada para operações em estruturas vazias (Pilha ou Fila).
 * Usa String e Herança de Exception, conforme permitido, utilizando 'throws' e 'try-catch'.
 */
class EstruturaVaziaException extends Exception {
    public EstruturaVaziaException(String mensagem) {
        // Usa a função super(String) conforme permitido.
        super(mensagem);
    }
}

/**
 * Classe Elemento: Contém os dados para Clientes (Fila) e Solicitações (Pilha).
 * Usa apenas tipos permitidos (String).
 */
class Elemento {
    String id;
    String info1; // Cliente: Nome / Pilha: Descrição
    String info2; // Cliente: Motivo / Pilha: Data/Hora

    /** Construtor para Clientes (Fila) */
    public Elemento(String id, String nomeCliente, String motivoAtendimento) {
        this.id = id;
        this.info1 = nomeCliente;
        this.info2 = motivoAtendimento;
    }

    /** Construtor para Solicitações (Pilha) */
    public Elemento(String idSolicitacao, String descricao, String dataHora) {
        this.id = idSolicitacao;
        this.info1 = descricao;
        this.info2 = dataHora;
    }

    public String paraPilhaString() {
        return "ID Solicitacao: " + id + " | Descricao: " + info1 + " | Data/Hora: " + info2;
    }

    public String paraFilaString() {
        return "ID Cliente: " + id + " | Nome: " + info1 + " | Motivo: " + info2;
    }
    
    // Método toString() auxiliar para logs e mensagens de remoção
    public String toString() {
        // Verifica o tipo do elemento para formatar corretamente
        if (id != null && id.length() >= 3 && id.substring(0, 3).equals("CLI")) {
            return paraFilaString();
        } else {
            return paraPilhaString();
        }
    }
}

/**
 * Classe Node: O componente básico da Lista Encadeada.
 * Implementação manual, sem uso de coleções prontas.
 */
class Node {
    Elemento dado;
    Node proximo; // A única referência usada para encadear a lista (corrigido de 'next')

    public Node(Elemento dado) {
        this.dado = dado;
        this.proximo = null;
    }
}

/**
 * Classe Pilha (LIFO): Implementada via Lista Encadeada (Node).
 */
class Pilha {
    private Node topo; 

    public Pilha() {
        this.topo = null;
    }

    public boolean estaVazia() {
        return topo == null;
    }

    /**
     * Adiciona um elemento ao topo da pilha (Push/Empilhar). O(1).
     */
    public void empilhar(Elemento elemento) {
        Node novoNode = new Node(elemento);
        novoNode.proximo = topo; 
        topo = novoNode;         
    }

    /**
     * Remove e retorna o elemento do topo da pilha (Pop/Desempilhar). O(1).
     * @throws EstruturaVaziaException Se a pilha estiver vazia.
     */
    public Elemento desempilhar() throws EstruturaVaziaException {
        if (estaVazia()) {
            throw new EstruturaVaziaException("Erro: Pilha de Histórico de Solicitações está vazia.");
        }
        Elemento elementoRemovido = topo.dado;
        topo = topo.proximo; 
        return elementoRemovido;
    }

    /**
     * Imprime todos os elementos (Histórico). Não usa 'length'.
     */
    public void visualizarHistorico() {
        System.out.println("\n=== HISTÓRICO DE SOLICITAÇÕES (Topo -> Base) ===");
        if (estaVazia()) {
            System.out.println("[Histórico vazio]");
            return;
        }

        Node atual = topo;
        int posicao = 1;
        // Percorre a lista até que o nó atual seja nulo
        while (atual != null) {
            System.out.println("[" + posicao + "] " + atual.dado.paraPilhaString());
            atual = atual.proximo;
            posicao++;
        }
    }
}

/**
 * Classe Fila (FIFO): Implementada via Lista Encadeada (Node).
 */
class Fila {
    private Node frente; // Início (Dequeue)
    private Node tras;   // Fim (Enqueue)

    public Fila() {
        this.frente = null;
        this.tras = null;
    }

    public boolean estaVazia() {
        return frente == null;
    }

    /**
     * Adiciona um elemento ao final da fila (Enqueue/Enfileirar). O(1).
     */
    public void enfileirar(Elemento elemento) {
        Node novoNode = new Node(elemento);

        if (estaVazia()) {
            frente = novoNode;
        } else {
            tras.proximo = novoNode;
        }
        tras = novoNode;
    }

    /**
     * Remove e retorna o elemento do início da fila (Dequeue/Desenfileirar). O(1).
     * @throws EstruturaVaziaException Se a fila estiver vazia.
     */
    public Elemento desenfileirar() throws EstruturaVaziaException {
        if (estaVazia()) {
            throw new EstruturaVaziaException("Erro: Fila de Atendimento está vazia.");
        }
        Elemento elementoAtendido = frente.dado;
        frente = frente.proximo;

        // Limpa o ponteiro 'tras' se a fila ficar vazia
        if (frente == null) {
            tras = null;
        }

        return elementoAtendido;
    }

    /**
     * Imprime todos os clientes (Ordem de Atendimento). Não usa 'length'.
     */
    public void visualizarOrdem() {
        System.out.println("\n=== Fila de Atendimento (Frente -> Trás) ===");
        if (estaVazia()) {
            System.out.println("[Fila vazia]");
            return;
        }

        Node atual = frente;
        int posicao = 1;
        // Percorre a lista até que o nó atual seja nulo
        while (atual != null) {
            System.out.println("[" + posicao + "] " + atual.dado.paraFilaString());
            atual = atual.proximo;
            posicao++;
        }
    }
}

/**
 * Classe Principal com a simulação do sistema e menu interativo.
 * Implementa try-catch para entrada e chamadas de métodos.
 */
public class GerenciamentoAtendimento {

    // Dados iniciais para a Fila
    private static final Elemento[] FILA_INICIAL = new Elemento[] {
        new Elemento("CLI001", "Maria Silva", "Dúvida sobre produto"),
        new Elemento("CLI002", "João Souza", "Reclamação de serviço"),
        new Elemento("CLI003", "Ana Costa", "Solicitação de reembolso"),
        new Elemento("CLI004", "Pedro Alves", "Informações de entrega"),
        new Elemento("CLI005", "Carla Dias", "Agendamento de visita"),
        new Elemento("CLI006", "Lucas Martins", "Alteração de pedido"),
        new Elemento("CLI007", "Patrícia Rocha", "Cancelamento de contrato"),
        new Elemento("CLI008", "Rafael Lima", "Renovação de assinatura"),
        new Elemento("CLI009", "Fernanda Gomes", "Suporte para instalação"),
        new Elemento("CLI010", "Carlos Eduardo", "Pedido de orçamento")
    };

    // Dados iniciais para o Histórico (Pilha)
    private static final Elemento[] HISTORICO_INICIAL = new Elemento[] {
        new Elemento("REQ001", "Instalação de software", "2024-08-20 10:30"),
        new Elemento("REQ002", "Manutenção preventiva", "2024-08-20 11:00"),
        new Elemento("REQ003", "Atualização de sistema", "2024-08-20 11:30"),
        new Elemento("REQ004", "Suporte técnico", "2024-08-20 12:00"),
        new Elemento("REQ005", "Troca de equipamento", "2024-08-20 12:30"),
        new Elemento("REQ006", "Consulta de garantia", "2024-08-20 13:00"),
        new Elemento("REQ007", "Reparo de impressora", "2024-08-20 13:30"),
        new Elemento("REQ008", "Configuração de rede", "2024-08-20 14:00"),
        new Elemento("REQ009", "Restauração de dados", "2024-08-20 14:30"),
        new Elemento("REQ010", "Consulta técnica", "2024-08-20 15:00")
    };

    public static void main(String[] args) {
        Fila filaAtendimento = new Fila();
        Pilha historicoSolicitacoes = new Pilha();
        Scanner scanner = new Scanner(System.in);
        
        // Carregar dados iniciais na Fila
        for (int i = 0; i < FILA_INICIAL.length; i++) {
            filaAtendimento.enfileirar(FILA_INICIAL[i]);
        }
        // Carregar dados iniciais na Pilha
        for (int i = 0; i < HISTORICO_INICIAL.length; i++) {
            historicoSolicitacoes.empilhar(HISTORICO_INICIAL[i]);
        }

        int opcao = -1;

        while (opcao != 0) {
            exibirMenu();
            
            try {
                System.out.print("Escolha uma opção: ");
                if (scanner.hasNextInt()) {
                    opcao = scanner.nextInt();
                    scanner.nextLine(); // Consumir a nova linha
                } else {
                    scanner.nextLine();
                    opcao = -1;
                    System.out.println("Entrada inválida. Por favor, digite um número.");
                    continue;
                }

                switch (opcao) {
                    case 1: // Ver fila de atendimento
                        filaAtendimento.visualizarOrdem();
                        break;
                    case 2: // Atender próximo cliente (e salvar no histórico)
                        atenderProximoCliente(filaAtendimento, historicoSolicitacoes);
                        break;
                    case 3: // Ver histórico de solicitações
                        historicoSolicitacoes.visualizarHistorico();
                        break;
                    case 4: // Adicionar solicitação ao histórico
                        adicionarSolicitacaoHistorico(historicoSolicitacoes, scanner);
                        break;
                    case 5: // Remover última solicitação do histórico
                        removerUltimaSolicitacao(historicoSolicitacoes);
                        break;
                    case 6: // Adicionar cliente à fila
                        adicionarClienteFila(filaAtendimento, scanner);
                        break;
                    case 7: // Verificar Status (opção extra para status)
                        verificarStatus(filaAtendimento, historicoSolicitacoes);
                        break;
                    case 0:
                        System.out.println("Sistema de Gerenciamento encerrado.");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
                System.out.println();

            } catch (InputMismatchException e) {
                // Tratamento de erro de entrada de Scanner (exigido try-catch)
                System.out.println("Erro: Entrada inválida. Por favor, use números para selecionar as opções.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }
    
    private static void exibirMenu() {
        System.out.println("\n==============================================");
        System.out.println("  SISTEMA DE GERENCIAMENTO DE ATENDIMENTO");
        System.out.println("==============================================");
        System.out.println("1. Ver fila de atendimento (Fila)");
        System.out.println("2. Atender próximo cliente (Desenfileirar/Dequeue)");
        System.out.println("3. Ver histórico de solicitações (Pilha)");
        System.out.println("4. Adicionar solicitação ao histórico (Empilhar/Push)");
        System.out.println("5. Remover última solicitação do histórico (Desempilhar/Pop)");
        System.out.println("6. Adicionar cliente à fila (Enfileirar/Enqueue)");
        System.out.println("7. Verificar Status (Fila e Pilha)");
        System.out.println("0. Sair");
        System.out.println("==============================================");
    }
    
    private static void atenderProximoCliente(Fila fila, Pilha historico) {
        try {
            // Desenfileira (lança exceção se vazia)
            Elemento atendido = fila.desenfileirar();
            System.out.println("\n--- ATENDIMENTO REALIZADO ---");
            System.out.println("Cliente Atendido: " + atendido.paraFilaString());
            
            // Transforma o atendimento em uma Solicitação e Empilha
            String novaIdReq = "REQ_AUTO_" + obterIDUnico();
            String dataHora = obterDataHoraSimples();
            
            Elemento novaSolicitacao = new Elemento(
                novaIdReq, 
                "Atendimento finalizado do cliente " + atendido.info1 + " (Motivo: " + atendido.info2 + ")", 
                dataHora
            );
            
            historico.empilhar(novaSolicitacao);
            System.out.println("Atendimento salvo no Histórico (ID: " + novaIdReq + ").");

        } catch (EstruturaVaziaException e) {
            // Captura a exceção da fila vazia (try-catch obrigatório)
            System.out.println(e.getMessage());
        }
    }

    private static void adicionarSolicitacaoHistorico(Pilha historico, Scanner scanner) {
        System.out.println("\n--- Adicionar Solicitação ao Histórico ---");
        System.out.print("ID da Solicitação (REQXXX): ");
        String id = scanner.nextLine();
        System.out.print("Descrição da Solicitação: ");
        String descricao = scanner.nextLine();
        
        String dataHora = obterDataHoraSimples(); 
        
        Elemento novaSolicitacao = new Elemento(id, descricao, dataHora);
        historico.empilhar(novaSolicitacao);
        System.out.println("Solicitação " + id + " adicionada ao Histórico.");
    }
    
    private static void removerUltimaSolicitacao(Pilha historico) {
        try {
            // Desempilha (lança exceção se vazia)
            Elemento removido = historico.desempilhar();
            System.out.println("\n--- REMOÇÃO DO HISTÓRICO ---");
            System.out.println("Removido do Histórico (Topo): " + removido.toString());
        } catch (EstruturaVaziaException e) {
            // Captura a exceção da pilha vazia (try-catch obrigatório)
            System.out.println(e.getMessage());
        }
    }

    private static void adicionarClienteFila(Fila fila, Scanner scanner) {
        System.out.println("\n--- Adicionar Cliente à Fila ---");
        System.out.print("ID do Cliente (CLIXXX): ");
        String id = scanner.nextLine();
        System.out.print("Nome do Cliente: ");
        String nome = scanner.nextLine();
        System.out.print("Motivo do Atendimento: ");
        String motivo = scanner.nextLine();

        Elemento novoCliente = new Elemento(id, nome, motivo);
        fila.enfileirar(novoCliente);
        System.out.println("Cliente " + nome + " (ID: " + id + ") adicionado à fila.");
    }

    private static void verificarStatus(Fila fila, Pilha historico) {
        System.out.println("\n--- STATUS DAS ESTRUTURAS ---");
        String statusFila = fila.estaVazia() ? "VAZIA" : "NÃO VAZIA";
        String statusPilha = historico.estaVazia() ? "VAZIA" : "NÃO VAZIA";
        
        System.out.println("Fila de Atendimento: " + statusFila);
        System.out.println("Histórico de Solicitações: " + statusPilha);
    }

    // Função auxiliar para gerar um ID baseado no tempo (apenas String/long permitido)
    private static String obterIDUnico() {
        return String.valueOf(System.currentTimeMillis() % 1000000);
    }
    
    // Função auxiliar para obter Data e Hora (apenas String permitido)
    private static String obterDataHoraSimples() {
        // Usa as classes de data e hora para obter o formato desejado (apenas o retorno é String)
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatador);
    }
}
