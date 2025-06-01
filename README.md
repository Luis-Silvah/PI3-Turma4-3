## SuperID - Projeto Integrador 03 

# Visão geral
O projeto integrador deste semestre consiste em desenvolver um ecossistema chamado gerenciador de autenticações. Isso significa desenvolver diversos processos que vão desde a criação de uma conta, armazenamento seguro de senhas e finalmente o uso das credenciais para realizar um login.
A complexidade desse assunto foi reduzida de forma que a concentração do aluno seja em aprender a construir um aplicativo para smartphones e sua integração com um backend na Internet. Este projeto tem finalidades educacionais, logo, não atende aos padrões mais exigentes de segurança da informação.
Com o objetivo de ter uma visão mais técnica, misturada com os requisitos funcionais, conforme as equipes evoluírem no conhecimento técnico sobre a linguagem Kotlin, Android e Google Firebase, se tornará mais fácil a compreensão e entendimento de todas os requisitos para a implementação.
Esse sistema conta com duas partes principais:
- Aplicativo Mobile (Android, desenvolvido em Kotlin), utilizado para a gestão e armazenamento seguro de credenciais do usuário final da solução (cliente final).
- Integração Web (via API e Firebase Functions), permitindo que sites parceiros utilizem o SuperID como método de login sem senha, além de outros autenticadores como Google etc 

### Algoritmo de criptografia de senhas
O AES, ou Padrão Avançado de Criptografia (Advanced Encryption Standard), é um algoritmo de criptografia simétrica que criptografa dados em blocos de 128 bits usando chaves de 128, 192 ou 256 bits. É um padrão amplamente adotado para proteger informações confidenciais, incluindo dados de governos e empresas. 

### Requisição criar site parceiro

```
curl -X POST http://127.0.0.1:5001/super-id-a0d24/us-central1/createSiteParceiro \
  -H "Content-Type: application/json" \
  -d '{
    "url": "www.example.com",
    "emailResponsavel": "responsavel@clinicavidaonline.com.br"
  }'
```