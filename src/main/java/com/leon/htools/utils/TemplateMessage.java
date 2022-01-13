package com.leon.htools.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public enum TemplateMessage {

    NICKNAME_LENGTH_NOT_SUPPORTED(
            new EmbedBuilder()
                    .setColor(new Color(212, 105, 105))
                    .addField("Erro:", "A quantidade de caracteres do nickname não é suportada.",
                              false
                    )),
    SERVER_NOT_FOUND(
            new EmbedBuilder()
                    .setColor(new Color(212, 105, 105))
                    .addField("Erro:", "O nome do servidor não corresponde à nenhum configurado.",
                              false
                    )),
    USER_NOT_FOUND(
            new EmbedBuilder()
                    .setColor(new Color(212, 105, 105))
                    .addField("Erro:", "Não foi encontrado nenhum usuário com este Discord ID.",
                              false
                    )),
    INTERNAL_ERROR(
            new EmbedBuilder()
                    .setColor(new Color(212, 105, 105))
                    .addField("Erro:", "Ocorreu um erro interno, por favor verifique o console.",
                              false
                    )),
    INFO_ATTACHED_SUCCESS(
            new EmbedBuilder()
                    .setColor(new Color(47, 49, 54))
                    .appendDescription("<:aprovado:890253815524241458> **Informações indexadas com sucesso!**\n")
                    .appendDescription("\n")
                    .appendDescription("Confirma as informações reconhecidas pelo nosso sistema de recrutamento.\n")
                    .appendDescription("Caso queira prosseguir com o processo basta selecionar em \"Enviar\".\n")
                    .appendDescription("\n")
                    .appendDescription("> **Informações recolhidas** \n")),
    INFO_CANCELED(
            new EmbedBuilder()
                    .setColor(new Color(47, 49, 54))
                    .appendDescription("<:negado:890649784250146836> **Processo cancelado com sucesso!**\n")
    ),
    INVITATION_ERROR(
            new EmbedBuilder()
                    .setColor(new Color(212, 105, 105))
                    .appendDescription("<:negado:890649784250146836> **Processo cancelado...**\n")
                    .appendDescription("\n")
                    .appendDescription("Oops, não consegui enviar a mensagem para o candidato.\n")
                    .appendDescription("\n")
                    .appendDescription("> É possível que o mesmo não esteja no discord / mensagens privadas abertas.")
    ),
    INVITATION_EXPIRE(
            new EmbedBuilder()
                    .setColor(new Color(47, 49, 54))
                    .setTitle("Convite de ingresso expirado")
                    .appendDescription("Olá, lamentamos informar que o seu recrutamento foi\n")
                    .appendDescription("cancelado devido ao prazo de resposta excedido.\n")
                    .appendDescription("\n")
                    .appendDescription("> Pedimos que esteja mais atento da próxima vez em")
                    .appendDescription(" todos os canais e mensagens privadas.\n")
                    .appendDescription("\n")
                    .appendDescription("Caso ainda esteja interessado para ingressar na nossa\n")
                    .appendDescription("equipe, sinta-se à vontade para refazer o formulário.\n")
                    .appendDescription("\n")
                    .appendDescription("Agradecemos pelo seu interesse em se candidatar.\n")
                    .appendDescription("Espero que entenda o motivo de tal decisão.\n")
                    .appendDescription("\n")
                    .appendDescription("Com os melhores cumprimentos,\n")
                    .appendDescription("Equipe de recrutamento.")
                    .setImage("https://minecraftskinstealer.com/achievement/2/Convite+expirado/Prazo+excedido.")
    ),
    INVITATION_RECEIVED(
            new EmbedBuilder()
                    .setColor(new Color(47, 49, 54))
                    .appendDescription("Olá, sou o responsável pelo processo de todos os recrutamentos no Hylex.\n")
                    .appendDescription("\n")
                    .appendDescription(
                            "<:aprovado:890253815524241458> **O seu formulário foi analisado e decidimos aceitar sua candidatura!**\n")
                    .appendDescription("\n")
                    .appendDescription(
                            "Você foi aceite na primeira etapa do processo seletivo de integração à equipe.\n")
                    .appendDescription("\n")
                    .appendDescription(
                            "Antes de tudo, esteja ciente de que todas as informações compartilhadas dentro do\n")
                    .appendDescription(
                            "processo seletivo e dentro da equipe nunca deverão ser divulgadas para o público.\n")
                    .appendDescription("\n")
                    .appendDescription(
                            "A partir do momento que aceitar iniciar a próxima etapa do processo seletivo, é\n")
                    .appendDescription("necessário que daqui em diante cumpra algumas responsabilidades e que tenha\n")
                    .appendDescription("total maturidade para agir de forma correta com todos os membros.\n")
                    .appendDescription("\n")
                    .setFooter("Caso decida dar continuidade basta selecionar no botão que indica sim. \n" +
                               "Porém, caso no momento já não esteja interessado clique em não.")
    ),
    REFUSE_INVITATION(
            new EmbedBuilder()
                    .setColor(new Color(47, 49, 54))
                    .setTitle("Convite de ingresso cancelado")
                    .appendDescription("Você cancelou a sua entrada na nossa equipe e\n")
                    .appendDescription("respeitamos a sua decisão plenamente.\n")
                    .appendDescription("\n")
                    .appendDescription("> Caso tenha recusado mas o motivo esteja")
                    .appendDescription(" relacionado com falta de tempo nas próximas")
                    .appendDescription(" semanas ou até dias contate o responsável pelo")
                    .appendDescription(" modo de jogo ao qual havia se candidatado.\n")
                    .appendDescription("\n")
                    .appendDescription("Agradecemos pelo seu interesse em se candidatar.\n")
                    .appendDescription("\n")
                    .appendDescription("Com os melhores cumprimentos,\n")
                    .appendDescription("Hylex Management Team.")
                    .setImage("https://minecraftskinstealer.com/achievement/2/Convite+cancelado/Recrutamento+negado")
    ),
    GENERATING_LINK(
            new EmbedBuilder()
                    .setColor(new Color(47, 49, 54))
                    .setThumbnail("https://cdn.discordapp.com/emojis/653399136737165323.gif?v=1")
                    .appendDescription("Recuperando o link do grupo de recrutamento para você.")
                    .setFooter("Aguarde uns instantes até o link ser recuperado no banco de dados.")
    ),
    GENERATED_LINK(
            new EmbedBuilder()
                    .setColor(new Color(47, 49, 54))
                    .appendDescription(
                            "<:aprovado:890253815524241458> **┆ Convite para o grupo de recrutamentos gerado com sucesso!**")
                    .setFooter("Entre no grupo assim que possível, o link é válido até 7 dias.")
    ),
    NO_PERMISSION(new EmbedBuilder()
                          .setColor(new Color(212, 105, 105))
                          .addField("Erro:", "Você não possui permissão para executar este comando.",
                                    false
                          )),
    EXPIRED(new EmbedBuilder()
                    .setColor(new Color(212, 105, 105))
                    .addField("Expirado:", "Você deixou o processo inativo por 90 segundos.",
                              false
                    )),
    CANCELED(new EmbedBuilder()
                     .setColor(new Color(212, 105, 105))
                     .addField("Cancelado:", "Você cancelou o seu relatório.",
                               false
                     )),
    NO_ARGS_REPORT(new EmbedBuilder()
                           .setColor(new Color(102, 180, 241))
                           .addField("Erro:", "Você não colocou nenhum título.", false)
                           .addField("Sugestão de comando:", "&reportar Digite o seu título", false)
    ),
    NO_ARGS_ATTACH(new EmbedBuilder()
                           .setColor(new Color(102, 180, 241))
                           .addField("Erro:", "Quantidade de argumentos não suportada", false)
                           .addField("Sugestão de comando:", "&anexar [reportId] [url] [descrição]", false)
    ),
    NO_ARGS_TAGCONTROL(new EmbedBuilder()
                               .setColor(new Color(102, 180, 241))
                               .addField("Erro:", "Quantidade de argumentos não suportada", false)
                               .addField("Sugestão de comando:", "&etiqueta (add/rem) [bugId] [roleId]", false)
    ),
    TEXT_LENGTH_NOT_SUPPORTED(new EmbedBuilder()
                                      .setColor(new Color(102, 180, 241))
                                      .addField("Erro:", "A quantidade de caracteres emitidas não é permitida.",
                                                false
                                      )
    ),
    HELP(
            new EmbedBuilder()
                    .setColor(new Color(102, 180, 241))
                    .setTitle("Confira os comandos disponíveis!")
                    .appendDescription("O sistema recolheu que você tem todas as\n")
                    .appendDescription("permissões possíveis, com isso você tem acesso aos\n")
                    .appendDescription("seguintes comandos. Confira-os:\n")
                    .appendDescription("\n")
                    .appendDescription("Comando: **&bug**\n")
                    .appendDescription("Inicia o processo de reporte de um erro do servidor;\n")
                    .appendDescription("\n")
                    .appendDescription("Comando: **&anexar**\n")
                    .appendDescription("Adiciona um link adicional a certo bug para permitir\n")
                    .appendDescription("enriquecer o conteúdo do reporte;\n")
                    .appendDescription("\n")
                    .appendDescription("Comando: **&etiqueta**\n")
                    .appendDescription("Configura os cargos a serem mencionados das\n")
                    .appendDescription("Categorias de Bugs.\n")

    ),
    NOT_URL(new EmbedBuilder()
                    .setColor(new Color(102, 180, 241))
                    .addField("Erro:", "O texto especificado não corresponde a uma url.", false)
    ),
    NOT_EXISTS_REPORT(new EmbedBuilder()
                              .setColor(new Color(102, 180, 241))
                              .addField("Erro:", "O id especificado não corresponde a nenhum relatório.", false)
    ),
    NO_STEPS(new EmbedBuilder()
                     .setColor(new Color(102, 180, 241))
                     .addField("Erro:", "Você não explicou como é feito o bug.", false)
                     .addField("Sugestão:", "\"Eu digitei blabla e apareceu um erro na tela.\"", false)
    ),
    REPORT_SAVE_ERROR(new EmbedBuilder()
                              .setColor(new Color(102, 180, 241))
                              .addField("Erro:", "Não foi possível salvar as alterações.", false)
    ),
    SAVE_SUCCESS(new EmbedBuilder()
                         .setColor(new Color(102, 180, 241))
                         .appendDescription("As alterações foram salvas com sucesso.")
    ),
    REPORT_SUCCESS(new EmbedBuilder()
                           .setColor(new Color(102, 180, 241))
                           .appendDescription("Olá, sou o responsável pela filtragem de bugs!")
                           .appendDescription("\n\n")
                           .appendDescription("**Obrigado por reportar!** O seu bug foi enviado")
                           .appendDescription("\n")
                           .appendDescription("para a nossa central de bugs e erros e em breve")
                           .appendDescription("\n")
                           .appendDescription("iremos analisá-lo de forma a implementarmos")
                           .appendDescription("\n")
                           .appendDescription("uma correção definitiva.")
                           .appendDescription("\n\n")
                           .appendDescription("Anexar ficheiros: ```&anexar [ID] [link] [desc].```")
                           .appendDescription("\n")
                           .appendDescription("Cuidado para não colocar uma descrição maior")
                           .appendDescription("\n")
                           .appendDescription("que 40 caracteres.")
                           .appendDescription("\n\n")
                           .appendDescription("Abaixo, encontra-se a forma de apresentação como")
                           .appendDescription("\n")
                           .appendDescription("o seu bug irá aparecer para toda a equipe.")
                           .setThumbnail("https://cdn.discordapp.com/emojis/822423373685850143.png?v=1")
    );

    private final EmbedBuilder embedBuilder;

    TemplateMessage(EmbedBuilder embedBuilder) {
        this.embedBuilder = embedBuilder;
    }

    public EmbedBuilder get() {
        return new EmbedBuilder(embedBuilder);
    }

    public MessageEmbed embed() {
        return this.embedBuilder.build();
    }

}
