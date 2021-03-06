/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package org.jenetics.xml;

import java.util.List;

import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.Genotype;
import org.jenetics.engine.Codec;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.Problem;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version 3.9
 * @since 3.9
 */
public class Usage {

	public static void main(final String[] args) throws Exception {
		final Problem<BitChromosome, BitGene, Integer> count = Problem.of(
			BitChromosome::bitCount,
			Codec.of(
				Genotype.of(BitChromosome.of(10)),
				gt -> gt.getChromosome().as(BitChromosome.class)
			)
		);

		final Engine<BitGene, Integer> engine = Engine.builder(count).build();

		final EvolutionResult<BitGene, Integer> result = engine.stream()
			.limit(10)
			.collect(EvolutionResult.toBestEvolutionResult());

		final List<Genotype<BitGene>> genotypes = result.getGenotypes();

		Writers.write(System.out, genotypes, Writers.BitChromosome.writer());
		Readers.read(System.in, Readers.BitChromosome.reader());

		engine.stream(genotypes);
	}

}
