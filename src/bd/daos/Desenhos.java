package bd.daos;

import java.sql.*;

import bd.BDMySQL;
import bd.dbos.Desenho;
import bd.core.*;

public class Desenhos
{
    public static boolean existe(String nome) throws Exception
    {
        boolean retorno;

        try
        {
            String sql;

            sql = "SELECT * " +
                  "FROM desenho " +
                  "WHERE nome = ?";

            BDMySQL.COMANDO.prepareStatement(sql);
            BDMySQL.COMANDO.setString(1, nome);
            MeuResultSet resultado = (MeuResultSet)BDMySQL.COMANDO.executeQuery();
            retorno = resultado.first();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new Exception(ex);
        }
        return retorno;
    }

    public static void inserir(Desenho desenho) throws Exception
    {
        if(desenho == null)
            throw new Exception ("Desenho inválido");
        
        try 
        {
            String sql;
            sql = "INSERT INTO desenhos " +
                  "(nome, cliente, criacao, atualizacao)"+
                  " VALUES"+
                  " (?, ?, ?, ?)";
            
            BDMySQL.COMANDO.prepareStatement (sql);

            BDMySQL.COMANDO.setString(1, desenho.getNome());
            BDMySQL.COMANDO.setString(2, desenho.getIp());
            BDMySQL.COMANDO.setDate(3, new Date(desenho.getCriacao().getTime()));
            BDMySQL.COMANDO.setDate(4, new Date(desenho.getAtualizacao().getTime()));

            BDMySQL.COMANDO.executeUpdate();
            BDMySQL.COMANDO.commit();
        }

        catch (SQLException erro)
        {
            erro.printStackTrace();
            BDMySQL.COMANDO.rollback();
            throw new Exception ("Erro ao inserir desenho");
        }
    
    }

    public static void excluir(Desenho desenho) throws Exception
    {
        if(desenho == null)
            throw new Exception ("Desenho inválido");
        
        try 
        {
            String sql;
            sql = "DELETE FROM DESENHOS "+
                  "WHERE nome = ?";

            BDMySQL.COMANDO.prepareStatement(sql);
            BDMySQL.COMANDO.setString(1, desenho.getNome());
            BDMySQL.COMANDO.executeUpdate();
            BDMySQL.COMANDO.commit();
        } 
        catch (SQLException erro) 
        {
            erro.printStackTrace();
            BDMySQL.COMANDO.rollback();
            throw new Exception ("Erro ao remover desenho");
        }
    }
    
        //recuperação de todos os desenhos do bd mas acho q não precisa, pq o cliente só pode pegar um por vez 
    public static MeuResultSet getDesenhos () throws Exception
    {
        MeuResultSet resultado = null;
        
        try 
        {
            String sql;

            sql = "SELECT *"+
                  "FROM desenho";

            BDMySQL.COMANDO.prepareStatement(sql);

            resultado = (MeuResultSet) BDMySQL.COMANDO.executeQuery ();
        } 
        catch (Exception e) 
        {
            throw new Exception ("Erro ao recuperar desenhos");
        }

        return resultado;
    }
    
        //recuperação de desenho expecifico no bd
    public static desenho getDesenho (String nome) throws Exception
    {
        Desenho desenho = null;
            //mudar pra código o parametro da função caso não funcione com o nome
        try 
        {
            String sql;

            sql = "SELECT *"+
                  "FROM desenho"+
                  "WHERE nome = ?";

            BDMySQL.COMANDO.prepareStatement(sql);

            BDMySQL.COMANDO.setNome (1, desenho.getNome());

            MeuResultSet resultado = (MeuResultSet) BDMySQL.COMANDO.executeQuery ();

            if (!resultado.first())
                throw new Exception ("Desenho não encontrado");
            
            desenho = new desenho (
                resultado.getNome ("nome"),
                reselutado.getCriacao ("criacao"),
                resultado.getAtualizacao ("atualizacao"),
                resultado.getIp ("ip");
            )
                
        } 
        catch (SQLException erro) 
        {
            throw new Exception ("Erro ao procurar por desenho");
        }

        return desenho;
    }
}
