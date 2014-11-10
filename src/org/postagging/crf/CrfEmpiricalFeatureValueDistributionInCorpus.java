package org.postagging.crf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.postagging.utilities.TaggedToken;
import org.postagging.utilities.PosTaggerException;
import static org.postagging.crf.CrfUtilities.safeAdd;


/**
 * 
 * @author Asher Stern
 * Date: Nov 9, 2014
 *
 * @param <K>
 * @param <G>
 */
public class CrfEmpiricalFeatureValueDistributionInCorpus<K,G>
{
	public CrfEmpiricalFeatureValueDistributionInCorpus(
			Iterator<? extends List<? extends TaggedToken<K, G>>> corpusIterator,
			ArrayList<CrfFeature<K, G>> features)
	{
		super();
		this.corpusIterator = corpusIterator;
		this.features = features;
	}



	public void calculate()
	{
		empiricalFeatureValue = new double[features.size()];
		for (int i=0;i<empiricalFeatureValue.length;++i) {empiricalFeatureValue[i]=0.0;}
		
		while (corpusIterator.hasNext())
		{
			List<? extends TaggedToken<K, G>> sentence = corpusIterator.next();
			K[] sentenceAsArray = CrfUtilities.extractSentence(sentence);
			int tokenIndex=0;
			G previousTag = null;
			for (TaggedToken<K, G> token : sentence)
			{
				Iterator<CrfFeature<K, G>> featureIterator = features.iterator();
				for (int featureIndex=0;featureIndex<empiricalFeatureValue.length;++featureIndex)
				{
					CrfFeature<K, G> feature = featureIterator.next();
					double featureValue = feature.value(sentenceAsArray,tokenIndex,token.getTag(),previousTag);
					empiricalFeatureValue[featureIndex] = safeAdd(empiricalFeatureValue[featureIndex], featureValue);
				}
				if (featureIterator.hasNext()) {throw new PosTaggerException("BUG");}
				
				++tokenIndex;
				previousTag = token.getTag();
			}
			if (tokenIndex!=sentence.size()) {throw new PosTaggerException("BUG");}
		}
	}
	
	

	public double[] getEmpiricalFeatureValue()
	{
		if (null==empiricalFeatureValue) {throw new PosTaggerException("Not calculated.");}
		return empiricalFeatureValue;
	}



	private final Iterator<? extends List<? extends TaggedToken<K, G>>> corpusIterator;
	private final ArrayList<CrfFeature<K, G>> features;

	private double[] empiricalFeatureValue = null;
}