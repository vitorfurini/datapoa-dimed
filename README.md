# datapoa-dimed

Descrição do projeto:

Este projeto foi desenvolvido para consumir a API datapoa/mobilidade, na qual permite que sejam consumidas informaçoes relativas a linhas e etinerários de ônibus.

O projeto conta com as seguintes tecnologias:
Java 11
Spring Boot(Hibernate e JPA)
REST
MySQL
Junit e Mockito
Swagger
Maven

Execução do projeto:
Para rodar o projeto é necessário ter instalado o Java JDK, Maven e o banco de dados MYSQL.

Baixe o projeto na sua máquina e abra na sua IDE de sua preferência.
Após buildar o projeto, execute e ele criará um banco de dados porém dará um erro pois não vai encontrar na base de dados as tabelas necessárias para execução.

Execute o arquivo createTables.sql no banco e tente novamente executar o projeto.

Para acessar o projeto, acesse o link http://localhost:8080/swagger-ui.html#/

Controllers:

LinhaController:

list - via GET - listagem de linhas de ônibus - mostra id, codigo e nome;

findByName - via POST - parâmetro nome - filtra todas as linhas pelo nome informado - mostra id, codigo e nome;

create - via POST - parâmetro objeto DTO linhaDTO - inclui codigo e nome no banco - primeiro pesquisa por codigo e nome na api externa e por última verifica no banco se dados não foram incluidos anteriormente;

update - via PUT - parâmetro objeto DTO linhaDTO - altera codigo e nome no banco - primeiro verifica se dados existem na base e após isso faz UPDATE nos dados;

delete - via DELETE - parâmetro ID da linha de ônibus - verifica se existe o dado informado e após isso faz DELETE nos dados(exclusão fisica);


ItinerarioController:


buscarPorLinha - via POST - parâmetro idlinha - filtra todos os itinerario de uma linha informada - mostra os dados da linha e latitude e longitude;

create - via POST - parâmetro objeto itinerarioDTO - inclui idlinha, latitude e longitude no banco - primeiro pesquisa por idlinha na api externa, não existindo latitude e longitude, verifica no banco se já não existe as mesmas localizações para mesmo idlinha, não encontrando esse dados faz INSERT dos dados;

update - via PUT - parâmetro objeto DTO itinerarioDTO - altera idlinha, latitude e longitude no banco - primeiro verifica se dados existem na base e após isso faz UPDATE nos dados;

delete - via DELETE - parâmetro ID do itinerario - verifica se existe o dado informado e após isso faz DELETE nos dados(exclusão fisica);

buscarRotas - via GET - parâmetros latitude, longitude e raio - faz busca usando a API PoaTransporte e retorna a existência de linhas de ônibus dentro do raio informado em KM.


TaxiController:
GET - pontoTaxi  - Retorna uma lista de pontos de taxi que foram salvos no Database.

GET - pontotaxi/{id} - Retorna uma lista de pontos de taxi que foram salvos no Database, a partir de seu ID.

DELETE - pontotaxi/{id} - Deleta ponto de taxi do Database a partir de seu ID.

POST - pontotaxi/save - Salva ou atualiza um ponto de taxi no Database. Se o ID for existente, atualiza nome e código. Se não houver o ID informado, cadastra uma nova linha.

GET - pontotaxi/txt - Retorna lista de pontos de taxi existente no arquivo TXT.

POST - pontotaxi/txt/save - Insere ponto de taxi na lista de pontos existentes no TXT e a retorna a lista atualizada. Parâmetro - String no formato: 

NOME_DO_PONTO#LATITUDE#LONGITUDE#DATA_HORA_CADASTRO.




