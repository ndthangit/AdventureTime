<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="Dungeon" tilewidth="16" tileheight="16" tilecount="48" columns="12">
 <image source="../Backgrounds/Tilesets/TilesetDungeon.png" width="192" height="64"/>
 <tile id="17">
  <properties>
   <property name="type" value="TRAP"/>
  </properties>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="0" width="16" height="16"/>
  </objectgroup>
  <animation>
   <frame tileid="16" duration="500"/>
   <frame tileid="17" duration="500"/>
  </animation>
 </tile>
</tileset>
