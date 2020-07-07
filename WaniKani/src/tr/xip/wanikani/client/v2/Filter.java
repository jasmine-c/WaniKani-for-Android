package tr.xip.wanikani.client.v2;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class Filter {

    public Map<String, String> filters;

    public Filter()
    {
        filters = new HashMap<>();
    }

    public Filter ids(Integer[] ids) {
        filters.put("ids", encodeParamsArray(ids));
        return this;
    }

    public Filter srs_stages(Integer[] srs_stages) {
        filters.put("srs_stages", encodeParamsArray(srs_stages));
        return this;
    }

    public Filter updated_after(DateTime updated_after)
    {
        filters.put("updated_after", updated_after.toString());
        return this;
    }

    public Filter types(String[] types)
    {
        filters.put("types", encodeParamsArray(types));
        return this;
    }

    public Filter slugs(String[] slugs)
    {
        filters.put("slugs", encodeParamsArray(slugs));
        return this;
    }

    public Filter levels(Integer[] levels) {
        filters.put("levels", encodeParamsArray(levels));
        return this;
    }

    public Filter hidden(Boolean hidden)
    {
        filters.put("hidden", hidden.toString());
        return this;
    }

    public Filter started(Boolean started)
    {
        filters.put("started", started.toString());
        return this;
    }

    public Filter burned(Boolean burned)
    {
        filters.put("burned", burned.toString());
        return this;
    }

    public Filter passed(Boolean passed)
    {
        filters.put("passed", passed.toString());
        return this;
    }

    public Filter unlocked(Boolean unlocked)
    {
        filters.put("unlocked", unlocked.toString());
        return this;
    }

    public Filter immediately_available_for_lessons()
    {
        filters.put("immediately_available_for_lessons", "");
        return this;
    }

    public Filter immediately_available_for_review()
    {
        filters.put("immediately_available_for_review", "");
        return this;
    }

    public Filter in_review()
    {
        filters.put("in_review", "");
        return this;
    }

    public Filter subject_ids(Integer[] subject_ids) {
        filters.put("subject_ids", encodeParamsArray(subject_ids));
        return this;
    }

    public Filter subject_types(String[] subject_types)
    {
        filters.put("subject_types", encodeParamsArray(subject_types));
        return this;
    }

    public Filter percentages_greater_than(Integer percentages_greater_than)
    {
        filters.put("percentages_greater_than", percentages_greater_than.toString());
        return this;
    }

    public Filter percentages_less_than(Integer percentages_less_than)
    {
        filters.put("percentages_less_than", percentages_less_than.toString());
        return this;
    }

    public Filter assignment_ids(Integer[] assignment_ids) {
        filters.put("assignment_ids", encodeParamsArray(assignment_ids));
        return this;
    }

    public Filter page_before_id(Integer page_before_id) {
        filters.put("page_before_id", Integer.toString(page_before_id));
        return this;
    }

    public Filter page_after_id(Integer page_after_id) {
        filters.put("page_after_id", Integer.toString(page_after_id));
        return this;
    }

    public Filter available_after(DateTime available_after)
    {
        filters.put("available_after", available_after.toString());
        return this;
    }

    public Filter available_before(DateTime available_before)
    {
        filters.put("available_before", available_before.toString());
        return this;
    }

    public static <T> String encodeParamsArray(T[] params)
    {
        StringBuilder builder = new StringBuilder();

        if (params.length == 0)
            return builder.toString();

        builder.append(params[0]);

        for (int i = 1; i < params.length; i++)
        {
            builder.append(',');
            builder.append(params[i]);
        }

        return builder.toString();
    }
}
