import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;
import com.mysql.jdbc.*;

public class SupervisoraDeConexao extends Thread
{
    private Parceiro usuario;
    private final Socket conexao;
    private final ArrayList<Parceiro> usuarios;

    public SupervisoraDeConexao (Socket conexao, ArrayList<Parceiro> usuarios) throws Exception
    {
        if (conexao == null)
            throw new Exception("Conexão ausente");

        if (usuarios == null)
            throw new Exception("Usuários ausentes");

        this.conexao = conexao;
        this.usuarios = usuarios;
    }

    public void run ()
    {
        ObjectOutputStream transmissor;
        try
        {
            transmissor = new ObjectOutputStream(this.conexao.getOutputStream());
        }
        catch (Exception erro)
        {
            erro.printStackTrace();
            return;
        }

        ObjectInputStream receptor;
        try
        {
            receptor = new ObjectInputStream(this.conexao.getInputStream());
        }
        catch (Exception erro)
        {
            try
            {
                transmissor.close();
            }
            catch (Exception ignored)
            {} // so tentando fechar antes de acabar a thread
            return;
        }

        try
        {
            this.usuario = new Parceiro(this.conexao, receptor, transmissor);
        }
        catch (Exception ignored)
        {} // sei que passei os parâmetros corretos

        try
        {
            synchronized (this.usuarios)
            {
                this.usuarios.add(this.usuario);
            }

            for(;;)
            {
                Comunicado comunicado = this.usuario.envie();

                if (comunicado == null)
                    return;
                else if (comunicado instanceof PedidoSalvamento) {
                    String nome = desenho.getNome();
                        if(nome.equals(ArrayList<String>desenhos(getDesenhos()))) {
                         String atualizacao = desenho;
                         atualizar(Desenho atualizacao);
                        } 
                        else((PedidoSalvamento) comunicado).salvar();                 
                }
                else if (comunicado instanceof PedidoParaSair) {
                    System.out.println("Saindo");
                    transmissor.close();
                    receptor.close();
                }
            }
        }
        catch (Exception erro)
        {
            try
            {
                transmissor.close();
                receptor.close();
            }
            catch (Exception ignored)
            {} // so tentando fechar antes de acabar a thread
        }
    }
}
