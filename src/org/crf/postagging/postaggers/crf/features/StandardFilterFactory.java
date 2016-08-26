package org.crf.postagging.postaggers.crf.features;

import java.util.LinkedHashSet;
import java.util.Set;

import org.crf.crf.filters.Filter;
import org.crf.crf.filters.FilterFactory;
import org.crf.crf.filters.TwoTagsFilter;

/**
 * A {@link FilterFactory} for the features generated by the {@link StandardFeatureGenerator}.
 * 
 * @author Asher Stern
 * Date: Nov 11, 2014
 *
 */
public class StandardFilterFactory implements FilterFactory<String, String>
{
	private static final long serialVersionUID = 6283122214266870374L;

	@Override
	public Set<Filter<String, String>> createFilters(String[] sequence, int tokenIndex, String currentTag, String previousTag)
	{
		String token = sequence[tokenIndex];
		Set<Filter<String, String>> ret = new LinkedHashSet<Filter<String,String>>();
		ret.add(new TwoTagsFilter<String, String>(currentTag, previousTag));
		//ret.add(new TokenAndTagFilter<String, String>(token, currentTag));
		ret.add(new CaseInsensitiveTokenAndTagFilter(token, currentTag));
		return ret;
	}
}