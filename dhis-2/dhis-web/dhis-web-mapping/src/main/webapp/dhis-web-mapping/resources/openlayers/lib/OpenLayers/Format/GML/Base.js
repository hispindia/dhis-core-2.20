if(!OpenLayers.Format.GML){OpenLayers.Format.GML={};}
OpenLayers.Format.GML.Base=OpenLayers.Class(OpenLayers.Format.XML,{namespaces:{gml:"http://www.opengis.net/gml",xlink:"http://www.w3.org/1999/xlink",xsi:"http://www.w3.org/2001/XMLSchema-instance",wfs:"http://www.opengis.net/wfs"},defaultPrefix:"gml",schemaLocation:null,featureType:null,featureNS:null,geometryName:"geometry",extractAttributes:true,srsName:null,xy:true,geometryTypes:null,singleFeatureType:null,regExes:{trimSpace:(/^\s*|\s*$/g),removeSpace:(/\s*/g),splitSpace:(/\s+/),trimComma:(/\s*,\s*/g)},initialize:function(options){OpenLayers.Format.XML.prototype.initialize.apply(this,[options]);this.setGeometryTypes();if(options&&options.featureNS){this.setNamespace("feature",options.featureNS);}
this.singleFeatureType=!options||(typeof options.featureType==="string");},read:function(data){if(typeof data=="string"){data=OpenLayers.Format.XML.prototype.read.apply(this,[data]);}
if(data&&data.nodeType==9){data=data.documentElement;}
var features=[];this.readNode(data,{features:features});if(features.length==0){var elements=this.getElementsByTagNameNS(data,this.namespaces.gml,"featureMember");if(elements.length){for(var i=0,len=elements.length;i<len;++i){this.readNode(elements[i],{features:features});}}else{var elements=this.getElementsByTagNameNS(data,this.namespaces.gml,"featureMembers");if(elements.length){this.readNode(elements[0],{features:features});}}}
return features;},readers:{"gml":{"featureMember":function(node,obj){this.readChildNodes(node,obj);},"featureMembers":function(node,obj){this.readChildNodes(node,obj);},"name":function(node,obj){obj.name=this.getChildValue(node);},"boundedBy":function(node,obj){var container={};this.readChildNodes(node,container);if(container.components&&container.components.length>0){obj.bounds=container.components[0];}},"Point":function(node,container){var obj={points:[]};this.readChildNodes(node,obj);if(!container.components){container.components=[];}
container.components.push(obj.points[0]);},"coordinates":function(node,obj){var str=this.getChildValue(node).replace(this.regExes.trimSpace,"");str=str.replace(this.regExes.trimComma,",");var pointList=str.split(this.regExes.splitSpace);var coords;var numPoints=pointList.length;var points=new Array(numPoints);for(var i=0;i<numPoints;++i){coords=pointList[i].split(",");if(this.xy){points[i]=new OpenLayers.Geometry.Point(coords[0],coords[1],coords[2]);}else{points[i]=new OpenLayers.Geometry.Point(coords[1],coords[0],coords[2]);}}
obj.points=points;},"coord":function(node,obj){var coord={};this.readChildNodes(node,coord);if(!obj.points){obj.points=[];}
obj.points.push(new OpenLayers.Geometry.Point(coord.x,coord.y,coord.z));},"X":function(node,coord){coord.x=this.getChildValue(node);},"Y":function(node,coord){coord.y=this.getChildValue(node);},"Z":function(node,coord){coord.z=this.getChildValue(node);},"MultiPoint":function(node,container){var obj={components:[]};this.readChildNodes(node,obj);container.components=[new OpenLayers.Geometry.MultiPoint(obj.components)];},"pointMember":function(node,obj){this.readChildNodes(node,obj);},"LineString":function(node,container){var obj={};this.readChildNodes(node,obj);if(!container.components){container.components=[];}
container.components.push(new OpenLayers.Geometry.LineString(obj.points));},"MultiLineString":function(node,container){var obj={components:[]};this.readChildNodes(node,obj);container.components=[new OpenLayers.Geometry.MultiLineString(obj.components)];},"lineStringMember":function(node,obj){this.readChildNodes(node,obj);},"Polygon":function(node,container){var obj={outer:null,inner:[]};this.readChildNodes(node,obj);obj.inner.unshift(obj.outer);if(!container.components){container.components=[];}
container.components.push(new OpenLayers.Geometry.Polygon(obj.inner));},"LinearRing":function(node,obj){var container={};this.readChildNodes(node,container);obj.components=[new OpenLayers.Geometry.LinearRing(container.points)];},"MultiPolygon":function(node,container){var obj={components:[]};this.readChildNodes(node,obj);container.components=[new OpenLayers.Geometry.MultiPolygon(obj.components)];},"polygonMember":function(node,obj){this.readChildNodes(node,obj);},"GeometryCollection":function(node,container){var obj={components:[]};this.readChildNodes(node,obj);container.components=[new OpenLayers.Geometry.Collection(obj.components)];},"geometryMember":function(node,obj){this.readChildNodes(node,obj);}},"feature":{"*":function(node,obj){var name;var local=node.localName||node.nodeName.split(":").pop();if(!this.singleFeatureType&&(OpenLayers.Util.indexOf(this.featureType,local)!=-1)){name="_typeName";}
else if(local==this.featureType){name="_typeName";}else{if(node.childNodes.length==0||(node.childNodes.length==1&&node.firstChild.nodeType==3)){if(this.extractAttributes){name="_attribute";}}else{name="_geometry";}}
if(name){this.readers.feature[name].apply(this,[node,obj]);}},"_typeName":function(node,obj){var container={components:[],attributes:{}};this.readChildNodes(node,container);if(container.name){container.attributes.name=container.name;}
var feature=new OpenLayers.Feature.Vector(container.components[0],container.attributes);if(!this.singleFeatureType){feature.type=node.nodeName.split(":").pop();feature.namespace=node.namespaceURI;}
var fid=node.getAttribute("fid")||this.getAttributeNS(node,this.namespaces["gml"],"id");if(fid){feature.fid=fid;}
if(this.internalProjection&&this.externalProjection&&feature.geometry){feature.geometry.transform(this.externalProjection,this.internalProjection);}
if(container.bounds){feature.geometry.bounds=container.bounds;}
obj.features.push(feature);},"_geometry":function(node,obj){this.readChildNodes(node,obj);},"_attribute":function(node,obj){var local=node.localName||node.nodeName.split(":").pop();var value=this.getChildValue(node);obj.attributes[local]=value;}},"wfs":{"FeatureCollection":function(node,obj){this.readChildNodes(node,obj);}}},write:function(features){var name;if(features instanceof Array){name="featureMembers";}else{name="featureMember";}
var root=this.writeNode("gml:"+name,features);this.setAttributeNS(root,this.namespaces["xsi"],"xsi:schemaLocation",this.schemaLocation);return OpenLayers.Format.XML.prototype.write.apply(this,[root]);},writers:{"gml":{"featureMember":function(feature){var node=this.createElementNSPlus("gml:featureMember");this.writeNode("feature:_typeName",feature,node);return node;},"MultiPoint":function(geometry){var node=this.createElementNSPlus("gml:MultiPoint");for(var i=0;i<geometry.components.length;++i){this.writeNode("pointMember",geometry.components[i],node);}
return node;},"pointMember":function(geometry){var node=this.createElementNSPlus("gml:pointMember");this.writeNode("Point",geometry,node);return node;},"MultiLineString":function(geometry){var node=this.createElementNSPlus("gml:MultiLineString");for(var i=0;i<geometry.components.length;++i){this.writeNode("lineStringMember",geometry.components[i],node);}
return node;},"lineStringMember":function(geometry){var node=this.createElementNSPlus("gml:lineStringMember");this.writeNode("LineString",geometry,node);return node;},"MultiPolygon":function(geometry){var node=this.createElementNSPlus("gml:MultiPolygon");for(var i=0;i<geometry.components.length;++i){this.writeNode("polygonMember",geometry.components[i],node);}
return node;},"polygonMember":function(geometry){var node=this.createElementNSPlus("gml:polygonMember");this.writeNode("Polygon",geometry,node);return node;},"GeometryCollection":function(geometry){var node=this.createElementNSPlus("gml:GeometryCollection");for(var i=0,len=geometry.components.length;i<len;++i){this.writeNode("geometryMember",geometry.components[i],node);}
return node;},"geometryMember":function(geometry){var node=this.createElementNSPlus("gml:geometryMember");var child=this.writeNode("feature:_geometry",geometry);node.appendChild(child.firstChild);return node;}},"feature":{"_typeName":function(feature){var node=this.createElementNSPlus("feature:"+this.featureType,{attributes:{fid:feature.fid}});if(feature.geometry){this.writeNode("feature:_geometry",feature.geometry,node);}
for(var name in feature.attributes){var value=feature.attributes[name];if(value!=null){this.writeNode("feature:_attribute",{name:name,value:value},node);}}
return node;},"_geometry":function(geometry){if(this.externalProjection&&this.internalProjection){geometry=geometry.clone().transform(this.internalProjection,this.externalProjection);}
var node=this.createElementNSPlus("feature:"+this.geometryName);var type=this.geometryTypes[geometry.CLASS_NAME];var child=this.writeNode("gml:"+type,geometry,node);if(this.srsName){child.setAttribute("srsName",this.srsName);}
return node;},"_attribute":function(obj){return this.createElementNSPlus("feature:"+obj.name,{value:obj.value});}},"wfs":{"FeatureCollection":function(features){var node=this.createElementNSPlus("wfs:FeatureCollection");for(var i=0,len=features.length;i<len;++i){this.writeNode("gml:featureMember",features[i],node);}
return node;}}},setGeometryTypes:function(){this.geometryTypes={"OpenLayers.Geometry.Point":"Point","OpenLayers.Geometry.MultiPoint":"MultiPoint","OpenLayers.Geometry.LineString":"LineString","OpenLayers.Geometry.MultiLineString":"MultiLineString","OpenLayers.Geometry.Polygon":"Polygon","OpenLayers.Geometry.MultiPolygon":"MultiPolygon","OpenLayers.Geometry.Collection":"GeometryCollection"};},CLASS_NAME:"OpenLayers.Format.GML.Base"});