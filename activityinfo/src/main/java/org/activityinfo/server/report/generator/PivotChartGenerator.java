package org.activityinfo.server.report.generator;

import com.google.inject.Inject;
import org.activityinfo.server.dao.PivotDAO;
import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.domain.Indicator;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.report.content.PivotChartContent;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.*;

public class PivotChartGenerator extends PivotGenerator<PivotChartElement> {

    protected final SchemaDAO schemaDAO;

    @Inject
    public PivotChartGenerator(PivotDAO pivotDAO, SchemaDAO schemaDAO) {
        super(pivotDAO);

        this.schemaDAO = schemaDAO;
    }

    @Override
    public void generate(User user, PivotChartElement element, Filter inheritedFilter,
                         DateRange dateRange) {

        if (element.getIndicators().size() == 0) {
            throw new ModelException("No indicators specified for chart", element);
        } else if (element.getIndicators().size() > 1 &&
                !element.allDimensionTypes().contains(DimensionType.Indicator)) {
            throw new ModelException("If multiple indicators are provided, either the category or legend dimension must be indicator.", element);
        }

        if (element.getSeriesDimension().size() > 0 &&
                element.getType() == PivotChartElement.Type.Bar) {

            throw new ModelException("Bar charts that are not stacked/clustered cannot have legend dimensions.", element);
        }


        Filter filter = resolveElementFilter(element, dateRange);
        Filter effectiveFilter = inheritedFilter == null ? new Filter(filter, new Filter()) : new Filter(inheritedFilter, filter);

        PivotTableData data = generateData(
                user.getLocaleObject(),
                element,
                effectiveFilter,
                element.getCategoryDimensions(),
                element.getSeriesDimension());

        ScaleUtil.Scale scale = computeScale(element, data);

        PivotChartContent content = new PivotChartContent();
        content.setXAxisTitle(composeXAxisTitle(element));
        content.setYAxisTitle(composeYAxisTitle(element));
        content.setEffectiveFilter(filter);
        content.setFilterDescriptions(generateFilterDescriptions(filter, element.allDimensionTypes(), user));
        content.setYMin(scale.valmin);
        content.setYStep(scale.step);
        content.setData(data);

        element.setContent(content);
    }

    private ScaleUtil.Scale computeScale(PivotChartElement element, PivotTableData data) {

        if (element.getType() == PivotChartElement.Type.Pie) {
            return new ScaleUtil.Scale();
        }

        // find min, max values
        PivotTableData.RangeCalculator rangeCalculator = new PivotTableData.RangeCalculator();
        data.visitAllCells(rangeCalculator);

        // anchor the y axis to zero.
        // TODO: check for cases where we don't want the axis to start at zero
        // e.g. non-sum, non-count indicators
        return ScaleUtil.computeScale(0, rangeCalculator.getMaxValue(), 5);
    }

    /**
     * Composes a title for the X Axis.
     *
     * @param element The <code>PivotChartElement</code> for which to compose the title
     * @return The category axis title, if explicitly specified, otherwise the name
     *         of the dimension type of the last category dimension
     */
    protected String composeXAxisTitle(PivotChartElement element) {

        if (element.getCategoryAxisTitle() != null) {
            return element.getCategoryAxisTitle();
        }

        if (element.getCategoryDimensions().size() == 0) {
            return null;
        }


        // TODO : localize
        return element.getCategoryDimensions()
                .get(element.getCategoryDimensions().size() - 1).getType().toString();

    }

    /**
     * @param element
     * @return The value axis title, if specified explicitly, otherwise the units
     *         of the first indicator referenced
     */
    protected String composeYAxisTitle(PivotChartElement element) {

        if (element.getValueAxisTitle() != null) {
            return element.getValueAxisTitle();
        }

        int indicatorId = element.getIndicators().get(0);

        Indicator indicator = schemaDAO.findById(Indicator.class, indicatorId);

        return indicator.getUnits();

    }


}
