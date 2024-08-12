package com.fmaupin.mspoc1.core.mapper;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.fmaupin.mspoc1.core.Constants;
import com.fmaupin.mspoc1.core.checker.rule.ASChecker;
import com.fmaupin.mspoc1.core.checker.rule.AndChecker;
import com.fmaupin.mspoc1.core.checker.rule.CheckerInputRuleManager;
import com.fmaupin.mspoc1.core.checker.rule.KeywordChecker;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.core.exception.CheckInputRuleException;
import com.fmaupin.mspoc1.core.enumeration.ExpressionEnum;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Exception lors d'une vérification d'une règle
 *
 * @author fmaupin, 17/12/2023
 * 
 * @since 0.0.1-SNAPSHOT
 * 
 *        mspoc1 is free software; you can redistribute it and/or
 *        modify it under the terms of the GNU Lesser General Public License as
 *        published by the Free Software Foundation; either version 3 of the
 *        License, or (at your option) any later version.
 *
 *        mspoc1 is distributed in the hope that it will be
 *        useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *        Lesser General Public License for more details.
 *
 *        You should have received a copy of the GNU Lesser General Public
 *        License along with this program; if not, write to the Free Software
 *        Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 *        02110-1301, USA.
 */
@Slf4j
public class InputRuleMapper implements MapObjectToMapMapper<String, ExpressionEnum, Object> {

    @Getter
    private CheckerInputRuleManager<String> checkerInputRuleManager = new CheckerInputRuleManager<>();

    public InputRuleMapper() {
        // enregister tous les checkers pour les règles input
        checkerInputRuleManager.register(new KeywordChecker()).register(new ASChecker()).register(new AndChecker());
    }

    @Override
    public Map<ExpressionEnum, Object> mapTo(String from) throws CheckInputRuleException {
        requireNonNull(from);

        // split la règle input en tokens
        List<String> exprs = Arrays.asList(from.split(Constants.TOKEN_REGEX));

        // vérifier règle input
        try {
            checkerInputRuleManager.apply(exprs);
        } catch (CheckInputRuleException e) {
            // erreur => les règles doivent être valides !
            log.error(e.getMessage());
            throw e;
        }

        // map les expressions
        EnumMap<ExpressionEnum, Object> map = new EnumMap<>(ExpressionEnum.class);

        map.put(ExpressionEnum.KEYWORD, HieroglyphEnum.valueOf(exprs.get(0)));

        IntStream.range(1, exprs.size()).forEach(i -> {
            // expression AS ?
            if (exprs.get(i).contains(ExpressionEnum.AS.toString())) {
                map.put(ExpressionEnum.AS, exprs.get(i + 1));
                i++;
            } else {
                // expression AND ?
                if (exprs.get(i).contains(ExpressionEnum.AND.toString())) {
                    map.put(ExpressionEnum.AND, exprs.get(i + 1));
                    i++;
                }
            }
        });

        return map;
    }

}
