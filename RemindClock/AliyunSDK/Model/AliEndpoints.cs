using System.Collections.Generic;

namespace AliyunSDK.Model
{
    public class AliEndpoints
    {
        public string RequestId { get; set; }
        public bool Success { get; set; }

        public AliEndpointArr Endpoints { get; set; }

        public class AliEndpointArr
        {
            public List<AliProtocol> Endpoint { get; set; }

        }

        public class AliProtocol
        {
            public List<string> Protocols { get; set; }

            public string Type { get; set; }

            public string Namespace { get; set; }
            public string Id { get; set; }
            public string SerivceCode { get; set; }
            public string Endpoint { get; set; }
        }
    }

}
