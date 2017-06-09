package samurai.items.decorator;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import samurai.database.Database;
import samurai.database.dao.ItemDao;
import samurai.items.DropTable;
import samurai.items.Item;
import samurai.items.ItemSlot;
import samurai.items.ItemUseContext;
import samurai.messages.base.SamuraiMessage;
import samurai.messages.impl.FixedMessage;
import samurai.messages.impl.util.Prompt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CrateVoucher extends ItemDecorator {
    public CrateVoucher(Item item) {
        super(item);
    }

    @Override
    protected SamuraiMessage use(ItemUseContext context) {
        final Emote emote = getData().getEmote();
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final StringBuilder sb = embedBuilder.getDescriptionBuilder();
        final double v = getData().getProperties()[0];
        if (context.getPointSession().getPoints() < v) {
            return FixedMessage.build("This crate requires **" + v + "** points to open.");
        }
        sb.append("Pay **")
                .append(v)
                .append("** points to open ")
                .append(getData().getName());
        final Message redeemPrompt = new MessageBuilder().setEmbed(
                embedBuilder
                        .setTitle("Please confirm")
                        .setThumbnail(emote.getImageUrl())
                        .setColor(getData().getRarity().getColor())
                        .setFooter(String.format("SlotId: %d | ItemId: %d", context.getItemSlot().getSlotId(), getData().getItemId()), null)
                        .build())
                .build();
        return new Prompt(redeemPrompt, prompt -> {
            prompt.getChannel().deleteMessageById(prompt.getMessageId()).queue();
            prompt.getChannel().sendMessage(open(context).getMessage()).queue();
        }, prompt -> prompt.getJDA().getTextChannelById(prompt.getChannelId()).deleteMessageById(prompt.getMessageId()).queue());
    }

    private FixedMessage open(ItemUseContext context) {
        final ItemSlot itemSlot = context.getItemSlot();
        if (itemSlot.getCount() == 1) {
            if (!context.getInventory().removeItemSlot(itemSlot)) return FixedMessage.build("This item has already been used");
        } else {
            itemSlot.offset(-1);
        }
        final DropTable dropTable = Database.get().<ItemDao, DropTable>openDao(ItemDao.class, itemDao -> itemDao.getDropTable(getData().getItemId()));
        final StringBuilder sb = new StringBuilder();
        context.getPointSession().offsetPoints(getData().getProperties()[0] * -1);
        for (int i = 0; i < getData().getProperties()[1]; i++) {
            final Item drop = dropTable.getDrop();
            context.getInventory().addItem(drop);
            sb.append(drop.getData().getEmote().getAsMention());
        }
        return FixedMessage.build(String.format("**%s** has paid %.2f points to open a %s_%s_ that contained %s", context.getMember().getEffectiveName(),getData().getProperties()[0], getData().getEmote().getAsMention(), getData().getName(), sb.toString()));
    }
}
