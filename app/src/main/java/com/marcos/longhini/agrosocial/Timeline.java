package com.marcos.longhini.agrosocial;

public class Timeline {
    private String nome;
    private String mensagem;
    private String dt;
    private String imagem64;


    public Timeline(String nome, String mensagem, String dt, String imagem64) {
        this.nome = nome;
        this.mensagem = mensagem;
        this.dt = dt;
        this.imagem64 = imagem64;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getImagem64() {
        return imagem64;
    }

    public void setImagem64(String imagem64) {
        this.imagem64 = imagem64;
    }
}
