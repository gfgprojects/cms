/*
Copyright 2017 Gianfranco Giulioni

This file is part of the Commodity Market Simulator (CMS):

    CMS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CMS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package cms.utils;
import cms.utils.ElementOfSupplyOrDemandCurve;

import java.util.Comparator;

public class ElementOfSupplyOrDemandCurveComparator implements Comparator<ElementOfSupplyOrDemandCurve> {

public int compare(ElementOfSupplyOrDemandCurve element1,ElementOfSupplyOrDemandCurve element2){
	return Double.compare(element1.getPrice(),element2.getPrice());
}
}
