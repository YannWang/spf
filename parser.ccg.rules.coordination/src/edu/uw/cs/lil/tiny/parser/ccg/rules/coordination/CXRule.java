/*******************************************************************************
 * UW SPF - The University of Washington Semantic Parsing Framework
 * <p>
 * Copyright (C) 2013 Yoav Artzi
 * <p>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 ******************************************************************************/
package edu.uw.cs.lil.tiny.parser.ccg.rules.coordination;

import java.util.Collection;
import java.util.Collections;

import edu.uw.cs.lil.tiny.ccg.categories.Category;
import edu.uw.cs.lil.tiny.ccg.categories.syntax.ComplexSyntax;
import edu.uw.cs.lil.tiny.ccg.categories.syntax.Slash;
import edu.uw.cs.lil.tiny.ccg.categories.syntax.Syntax;
import edu.uw.cs.lil.tiny.parser.ccg.rules.IBinaryParseRule;
import edu.uw.cs.lil.tiny.parser.ccg.rules.ParseRuleResult;
import edu.uw.cs.lil.tiny.parser.ccg.rules.RuleName;
import edu.uw.cs.lil.tiny.parser.ccg.rules.RuleName.Direction;
import edu.uw.cs.utils.collections.ListUtils;

class CXRule<MR> implements IBinaryParseRule<MR> {
	
	private static final RuleName			RULE_NAME	= RuleName
																.create("cx",
																		Direction.FORWARD);
	
	private final ICoordinationServices<MR>	services;
	
	public CXRule(ICoordinationServices<MR> services) {
		this.services = services;
	}
	
	@Override
	public Collection<ParseRuleResult<MR>> apply(Category<MR> left,
			Category<MR> right) {
		if (left.getSyntax() instanceof ComplexSyntax
				&& ((ComplexSyntax) left.getSyntax()).getSlash().equals(
						Slash.FORWARD) && right.getSem() != null
				&& left.getSem() != null) {
			final Syntax argType = ((ComplexSyntax) left.getSyntax())
					.getRight();
			if (SyntaxCoordinationServices.isCoordinationOfType(
					right.getSyntax(), argType)) {
				final MR applied = services.applyCoordination(left.getSem(),
						right.getSem());
				if (applied != null) {
					return ListUtils
							.createSingletonList(new ParseRuleResult<MR>(
									RULE_NAME, Category.create(
											((ComplexSyntax) left.getSyntax())
													.getLeft(), applied)));
				}
			}
		}
		return Collections.emptyList();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		final CXRule other = (CXRule) obj;
		if (services == null) {
			if (other.services != null) {
				return false;
			}
		} else if (!services.equals(other.services)) {
			return false;
		}
		return true;
	}
	
	@Override
	public RuleName getName() {
		return RULE_NAME;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((RULE_NAME == null) ? 0 : RULE_NAME.hashCode());
		result = prime * result
				+ ((services == null) ? 0 : services.hashCode());
		return result;
	}
	
}
