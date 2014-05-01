/*
 * Copyright (c) 2014 Villu Ruusmann
 *
 * This file is part of JPMML-Evaluator
 *
 * JPMML-Evaluator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JPMML-Evaluator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with JPMML-Evaluator.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jpmml.evaluator;

import java.util.*;

import org.dmg.pmml.*;

import org.junit.*;

import static org.junit.Assert.*;

public class ModelEvaluationContextTest extends ModelEvaluatorTest {

	@Test
	public void evaluate() throws Exception {
		ModelEvaluator<?> evaluator = createModelEvaluator(AssociationAlgorithmTest.class);

		FieldName item = new FieldName("item");
		List<String> itemValue = Arrays.asList("Cracker", "Water", "Coke");

		FieldName dummy = new FieldName("dummy");
		String dummyValue = "Dummy";

		DataDictionary dataDictionary = evaluator.getDataDictionary()
			.withDataFields(new DataField(dummy, OpType.CATEGORICAL, DataType.STRING));

		Output output = evaluator.getOutput()
			.withOutputFields(createArgumentCopy(item, null), createArgumentCopy(dummy, "missing"));

		Map<FieldName, ?> arguments = createArguments(item, EvaluatorUtil.prepare(evaluator, item, itemValue), dummy, dummyValue);

		Map<FieldName, ?> result = evaluator.evaluate(arguments);

		assertEquals(itemValue, result.get(new FieldName("item_copy")));
		assertEquals("missing", result.get(new FieldName("dummy_copy")));
	}

	static
	private OutputField createArgumentCopy(FieldName name, String mapMissingTo){
		FieldRef fieldRef = new FieldRef(name)
			.withMapMissingTo(mapMissingTo);

		OutputField result = new OutputField()
			.withName(FieldName.create(name.getValue() + "_copy"))
			.withFeature(ResultFeatureType.TRANSFORMED_VALUE)
			.withExpression(fieldRef);

		return result;
	}
}