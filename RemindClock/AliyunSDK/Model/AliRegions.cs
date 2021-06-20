using System.Collections.Generic;

namespace AliyunSDK.Model
{
    public class AliRegions
    {
        public string RequestId { get; set; }

        public RegionArr Regions { get; set; }

        public class RegionArr
        {
            public List<Regions_Region> Region { get; set; }

        }
        public class Regions_Region
        {
            public string RegionId { get; set; }

            public string LocalName { get; set; }

            public string Status { get; set; }
        }
    }

}
