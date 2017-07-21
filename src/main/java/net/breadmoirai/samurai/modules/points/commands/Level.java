/*
 *         Copyright 2017 Ton Ly (BreadMoirai)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package net.breadmoirai.samurai.modules.points.commands;

import net.breadmoirai.samurai.modules.points.LevelStatus;
import net.breadmoirai.samurai.modules.points.PointModule;
import net.breadmoirai.samurai.util.CommandEventUtil;
import net.breadmoirai.sbf.core.CommandEvent;
import net.breadmoirai.sbf.core.command.Key;
import net.breadmoirai.sbf.core.command.ModuleCommand;
import net.breadmoirai.sbf.core.response.Response;
import net.breadmoirai.sbf.core.response.Responses;
import net.dv8tion.jda.core.entities.Member;

@Key("level")
public class Level extends ModuleCommand<PointModule> {

    private static final String FILL = "\u2588", BORDER = "\u2502";

    @Override
    public Response execute(CommandEvent event, PointModule module) {
        final Member member = CommandEventUtil.getSpecifiedMember(event);
        final LevelStatus levelStatus = LevelStatus.fromSession(module.getPointSession(member));
        final double progress = levelStatus.getExpProgress() * 100 / 4;
        return Responses.ofFormat("**%s**%n" +
                        "Points:`%.2f`    Exp:`%.2f`%n" +
                        "Level:`%d`                (`%.0f`/`%.0f`)%n" +
                        "`%s%-25s%7$s`",
                member.getEffectiveName(),
                levelStatus.getPoints(), levelStatus.getExp(),
                levelStatus.getLevel(), levelStatus.getExpAtThisLevel(), levelStatus.getTotalExpRequiredForLevel(),
                BORDER, progress == 0 ? "" : String.format("%" + progress + "s", "").replace(" ", FILL));
    }
}
