package com.leon.htools.utils;

import com.leon.htools.api.entities.Recruitment;
import com.leon.htools.config.Config;
import com.leon.htools.config.Gamemode;
import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Calendar;

@UtilityClass
public class TemplateMessages {

    public Message infoAttached(Recruitment recruitment, Config config) {
        final MessageBuilder builder = new MessageBuilder();
        final Gamemode gamemode = config.getRecruitmentSystem().getGamemodes().stream()
                                        .filter(o -> o.getName()
                                                      .equalsIgnoreCase(recruitment.getGamemode()))
                                        .findFirst().get();
        final EmbedBuilder embedBuilder = TemplateMessage.INFO_ATTACHED_SUCCESS
                .get()
                .appendDescription("> `Nickname`: " + recruitment.getNickname() + "\n")
                .appendDescription("> `Modo de Jogo`: " + gamemode.getLabel() + "\n")
                .appendDescription("> `Discord`: " + recruitment.getApplicantAsMention() + "\n")
                .appendDescription("> `Discord ID`: " + recruitment.getApplicantId());
        builder
                .setEmbeds(embedBuilder.build())
                .allowMentions(Message.MentionType.USER, Message.MentionType.EMOTE)
                .mentionUsers(recruitment.getApplicantId());
        return builder.build();
    }

    public Message invitationReceived(Recruitment recruitment, Config config) {
        final MessageBuilder builder = new MessageBuilder();
        final Gamemode gamemode = config.getRecruitmentSystem().getGamemodes().stream()
                                        .filter(o -> o.getName()
                                                      .equalsIgnoreCase(recruitment.getGamemode()))
                                        .findFirst().get();
        final EmbedBuilder embedBuilder = TemplateMessage.INVITATION_RECEIVED
                .get()
                .appendDescription("> **Informa????es recolhidas** \n")
                .appendDescription("> `Nickname`: " + recruitment.getNickname() + "\n")
                .appendDescription("> `Modo de Jogo`: " + gamemode.getLabel() + "\n")
                .appendDescription("\n")
                .appendDescription("> **NOTA**: Caso pretenda continuar com o recrutamento tudo estar?? explicado no")
                .appendDescription(" grupo de recrutados, por favor leia tudo com muita aten????o e evite perguntas")
                .appendDescription(" desnecess??rias. Inicialmente s?? ter?? acesso ao canal de verifica????o por cerca de")
                .appendDescription(" tr??s minutos onde ter?? acesso ??s informa????es iniciais.\n")
                .appendDescription("**\n**");
        builder
                .setContent("")
                .setEmbeds(embedBuilder.build())
                .allowMentions(Message.MentionType.USER, Message.MentionType.EMOTE)
                .mentionUsers(recruitment.getApplicantId());
        return builder.build();
    }

    public Message controlPanel(Recruitment recruitment, Config config) {
        final MessageBuilder builder = new MessageBuilder();
        final Gamemode gamemode = config.getGamemode(recruitment.getGamemode());
        final EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(new Color(47, 49, 54))
                .setTitle("Configura????es - Recrutamento")
                .appendDescription(
                        "O candidato `" + recruitment.getNickname() + "` foi adicionado ao sistema de recrutamento.\n")
                .appendDescription("\n")
                .appendDescription(
                        "Todas as etapas do recrutamento s??o controladas a partir deste painel de controle.\n")
                .appendDescription("\n")
                .appendDescription(
                        "> **STATUS**: " + recruitment.getStage().getLabel() + " (_" + gamemode.getLabel() + "_).")
                .setFooter("Use os bot??es abaixo para controlar o sistema.");
        builder
                .setEmbeds(embedBuilder.build())
                .allowMentions(Message.MentionType.USER, Message.MentionType.EMOTE)
                .mentionUsers(recruitment.getApplicantId());
        return builder.build();
    }

    public Message generatedLink(Calendar calendar) {
        final EmbedBuilder builder = TemplateMessage.GENERATED_LINK.get();
        builder.setTimestamp(calendar.toInstant());
        return new MessageBuilder(builder).build();
    }

    public String accepted(User user) {
        return "Ol??, " + user.getAsMention() + "!\n" +
               "\n" +
               "> Voc?? foi **aprovado** em nosso processo seletivo de recrutamento, parab??ns!\n" +
               "> \n" +
               "> Previamente, ?? importante ressaltarmos de que tenha o m??nimo de conhecimento em sua ??rea apresentada para que fa??a-se um bom trabalho em nossa comunidade! Portanto, pedimos que a introdu????o ?? equipe e as regras do nosso servidor sejam lidas (obrigat??rio).\n" +
               "> \n" +
               "> **1.** `Documento introdut??rio ?? equipe (n??o divulgue este material):` <https://equipe.hylex.me/informacoes/intro>;\n" +
               "> **2.** `Regras do servidor:` <https://hylex.net/rules>.\n" +
               "> \n" +
               "> ?? importante tamb??m ressaltarmos a necessidade de uma comunica????o formal perante os jogadores, principalmente por sermos os membros da equipe com maior intera????o com a comunidade. ?? importante mantermos uma identidade profissional para ressaltarmos o nosso comprometimento com todos. \n" +
               "> \n" +
               "> Para al??m da formalidade, ?? extremamente importante esclarecer d??vidas. Nunca, mas nunca responda a um jogador com uma informa????o que voc?? n??o possui certeza se ?? a correta. Nestes casos, voc?? deve falar com o respons??vel do modo de jogo a que voc?? pertence, ou esclarecer no canal de d??vidas gerais da equipe. \n" +
               "> \n" +
               "> Para al??m disso, algumas informa????es adicionais:\n" +
               ">   \n" +
               ">  **1.** N??o ?? obrigat??rio falar formalmente no grupo oficial da equipe.\n" +
               ">  **2.** ?? obrigat??rio a autentica????o de 2 fatores (2FA) no servidor do Hylex e no seu Discord. (Ferramentas: __Google Auth, MYKI__)\n" +
               "> \n" +
               "> No momento, s??o apenas estas informa????es que desejamos repassar a voc?? antes que ingresse no discord oficial da Equipe do Hylex.\n" +
               "\n" +
               "**NOTA**: Ap??s confirmar a leitura de ambos os documentos e ativar o 2FA, poderemos prosseguir.";
    }

    public String acceptedStudios(User user) {
        return "Ol??, " + user.getAsMention() + "!\n" +
               "\n" +
               "> Voc?? foi **aprovado** em nosso processo seletivo de recrutamento no Hylex Studios!\n" +
               "> \n" +
               "> Previamente, ?? importante ressaltarmos de que tenha o m??nimo de conhecimento em sua ??rea apresentada para que fa??a-se um bom trabalho em nossa comunidade! Portanto, pedimos que a introdu????o ?? equipe e as regras do nosso servidor sejam lidas (obrigat??rio).\n" +
               "> \n" +
               "> **1.** `Documento introdut??rio ao Hylex Studios (n??o divulgue este material):` <https://studios.hylex.me>\n" +
               "> **2.** `Regras do servidor:` <https://hylex.net/rules>\n" +
               "> \n" +
               "> ?? importante tamb??m ressaltarmos a necessidade de uma comunica????o formal perante os jogadores, principalmente pelo fato de sempre estar a representar o Hylex Studios comunidade ?? fora. ?? importante mantermos uma identidade profissional para ressaltarmos o nosso comprometimento com toda a comunidade.\n" +
               "> \n" +
               "> No momento, s??o apenas estas informa????es que desejamos repassar a voc?? antes que ingresse no discord do Hylex Studios.\n" +
               "\n" +
               "**NOTA**: Ap??s confirmar a leitura de ambos os documentos, poderemos prosseguir.";
    }

    public String refused(User user) {
        return "Ol??, " + user.getAsMention() + "!\n" +
               "\n" +
               "Lamentamos, mas voc?? foi **negado** no processo seletivo do nosso recrutamento.\n" +
               "\n" +
               "Voc?? infelizmente foi reprovado na ??ltima etapa do processo seletivo de equipe do Hylex. \n" +
               "Gostar??amos de agradecer a voc?? pelo seu esfor??o e resili??ncia em chegar na ??ltima etapa, muitos n??o conseguem tal feito. \n" +
               "\n" +
               "**NOTA**: Ainda ser?? poss??vel participar do processo seletivo, o tempo de envio de um novo formul??rio para uma nova tentativa ?? de uma semana. Acreditamos que todos voc??s podem ter potencial se o almejarem. Apenas ?? quest??o de esfor??o, reflex??o e dedica????o. Recomendamos sempre a leitura completa das informa????es de ingresso, ou seja, as habilidades / requisitos necess??rios, dicas de ingresso.\n" +
               "\n" +
               "At?? uma pr??xima, :wave:.";
    }


}
