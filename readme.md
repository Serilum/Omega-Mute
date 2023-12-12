<h2>Omega Mute</h2>
<p><a href="https://github.com/Serilum/Omega-Mute"><img src="https://serilum.com/assets/data/logo/omega-mute.png"></a></p><h2>Download</h2>
<p>You can download Omega Mute on CurseForge and Modrinth:</p><p>&nbsp;&nbsp;CurseForge: &nbsp;&nbsp;<a href="https://curseforge.com/minecraft/mc-mods/omega-mute">https://curseforge.com/minecraft/mc-mods/omega-mute</a><br>&nbsp;&nbsp;Modrinth: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="https://modrinth.com/mod/omega-mute">https://modrinth.com/mod/omega-mute</a></p>
<h2>Issue Tracker</h2>
<p>To keep a better overview of all mods, the issue tracker is located in a separate repository.<br>&nbsp;&nbsp;For issues, ideas, suggestions or anything else, please follow this link:</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;-> <a href="https://serilum.com/url/issue-tracker">Issue Tracker</a></p>
<h2>Pull Requests</h2>
<p>Because of the way mod loader files are bundled into one jar, some extra information is needed to do a PR.<br>&nbsp;&nbsp;A wiki page entry about it is available here:</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;-> <a href="https://serilum.com/url/pull-requests">Pull Request Information</a></p>
<h2>Mod Description</h2>
<p><a href="https://serilum.com/" rel="nofollow"><img src="https://github.com/Serilum/.cdn/blob/main/description/header/header.png" alt="" width="838" height="400"></a><br><br><a href="https://legacy.curseforge.com/minecraft/mc-mods/omega-mute/files"><img src="https://github.com/Serilum/.cdn/raw/main/description/versions/header.png"></a><a href="https://legacy.curseforge.com/minecraft/mc-mods/omega-mute/files/all?filter-status=1&filter-game-version=1738749986:75125" rel="nofollow"><img src="https://github.com/Serilum/.cdn/raw/main/description/versions/1_20.png"></a><a href="https://legacy.curseforge.com/minecraft/mc-mods/omega-mute/files/all?filter-status=1&filter-game-version=1738749986:73407" rel="nofollow"><img src="https://github.com/Serilum/.cdn/raw/main/description/versions/1_19.png"></a><a href="https://legacy.curseforge.com/minecraft/mc-mods/omega-mute/files/all?filter-status=1&filter-game-version=1738749986:73250" rel="nofollow"><img src="https://github.com/Serilum/.cdn/raw/main/description/versions/1_18.png"></a><a href="https://legacy.curseforge.com/minecraft/mc-mods/omega-mute/files/all?filter-status=1&filter-game-version=1738749986:73242" rel="nofollow"><img src="https://github.com/Serilum/.cdn/raw/main/description/versions/1_17.png"></a><a href="https://legacy.curseforge.com/minecraft/mc-mods/omega-mute/files/all?filter-status=1&filter-game-version=1738749986:70886" rel="nofollow"><img src="https://github.com/Serilum/.cdn/raw/main/description/versions/1_16.png"></a><a href="https://legacy.curseforge.com/minecraft/mc-mods/omega-mute/files/all?filter-status=1&filter-game-version=1738749986:68722" rel="nofollow"><img src="https://github.com/Serilum/.cdn/raw/main/description/versions/1_15.png"></a><a href="https://legacy.curseforge.com/minecraft/mc-mods/omega-mute/files/all?filter-status=1&filter-game-version=1738749986:64806" rel="nofollow"><img src="https://github.com/Serilum/.cdn/raw/main/description/versions/1_14.png"></a><br><br><strong><span style="font-size:24px">Requires the library mod&nbsp;<a style="font-size:24px" href="https://www.curseforge.com/minecraft/mc-mods/collective" rel="nofollow">Collective</a>.</span></strong><strong>&nbsp;<br><br> &nbsp; &nbsp;This mod is part of <span style="color:#008000"><a style="color:#008000" href="https://curseforge.com/minecraft/modpacks/the-vanilla-experience" rel="nofollow">The Vanilla Experience</a></span>.</strong><br><span style="font-size:18px">Omega Mute is a minimalistic <strong><strong>client-side</strong></strong> mod which allows you to mute/silence all Minecraft sounds while in-game or via a file. You won't need anything else to take full control over all in-game sounds. This <strong>works for modded sounds</strong> as well. Just place an exclamation mark in front of the sound you want to mute inside the config soundmap file, or use the commands.<br><br>The mod also contains a sound culling feature. With this you can for example allow a sound to play every 10 seconds. This still allows being immersed into the game without having the same sound over and over and over again when near a source.<br></span><br><br><strong>The mod will create a soundmap text file in<em> ./config/omegamute/</em> containing all the sound names, which are generated upon first load of the mod.</strong><br><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/omega-mute/a.jpg" alt="soundmap_location" width="657" height="548"></picture><br><strong><br>You can manually edit the file and edit the sounds you want to mute. -- Or use the commands below.<br><span style="font-size:18px">An exclamation mark " ! " mutes the sound always.</span><br><span style="font-size:18px">Adding " 10- " in front of a line allows the sound to play every 10 seconds.</span><br>After which you can either do the command '<em>/omegamute reload</em>' in-game, or restart your client/server. (GIF:)<br></strong></p>
<div class="spoiler">
<p><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/omega-mute/b.gif" alt="gif0" width="652" height="548"></picture></p>
</div>
<p>&nbsp;<br><br></p>
<p><strong><span style="font-size:24px">The mod also contains commands to (un)mute, reload, query and listen:</span><br></strong><span style="font-size:14px">/omegamute reload - reloads all manual changes in the soundmap.txt.</span><br><span style="font-size:14px">/omegamute query - shows all sounds currently muted.</span><br><span style="font-size:14px">/omegamute listen&nbsp;-&nbsp;<strong>toggles</strong> listening to all sound events.</span><br><span style="font-size:14px">/omegamute mute &lt;string&gt; - mutes all sounds which contain &lt;string&gt;<br>/omegamute cull &lt;integer&gt; &lt;string&gt; - culls all sounds which contain &lt;string&gt; in between &lt;integer&gt; seconds</span><br><span style="font-size:14px">/omegamute unmute &lt;string&gt; - unmutes all sounds which contain &lt;string&gt;</span><br><br><br><span style="font-size:18px"><strong>You can also reload the sound file with a hotkey. By default it's a dot, but you can set this in the settings menu:</strong></span><br><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/omega-mute/c.jpg" width="1142" height="650"></picture><br><br><span style="font-size:18px"><strong>Listening to sounds:</strong></span><br><strong>With&nbsp;<em>/omegamute listen</em>, all sounds around will be sent to the player in a message:<br></strong></p>
<div class="spoiler">
<p><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/omega-mute/d.gif" alt="gif_a" width="1000" height="594"></picture></p>
</div>
<p>&nbsp;<br><strong>By using the command&nbsp;<em>/omegamute listen</em> again, it turns off:<br></strong></p>
<div class="spoiler">
<p><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/omega-mute/e.gif" alt="gif_b" width="1000" height="596"></picture></p>
</div>
<p>&nbsp;</p>
<p>&nbsp;<br><span style="font-size:18px"><strong>Muting sounds:</strong></span><br><strong>If you want to mute all slime entities, you can do '<em>/omegamute mute entity.slime</em>':<br></strong></p>
<div class="spoiler">
<p><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/omega-mute/f.gif" alt="gif_c" width="1000" height="596"></picture></p>
</div>
<p>&nbsp;</p>
<p><strong>Because all sound names contain a period (dot .), you can unmute (or mute) all sounds by using the . as a string. In this case it unmutes the slimes previously muted:<br></strong></p>
<div class="spoiler">
<p><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/omega-mute/g.gif" alt="gif_d" width="1000" height="596"></picture></p>
</div>
<p>&nbsp;</p>
<p><strong><br></strong><strong><span style="font-size:18px">Via '<em>/omegamute query</em>' you can see all sounds currently muted:</span><br><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/omega-mute/h.png" width="483" height="45"></picture><br></strong></p>
<p><strong><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/omega-mute/i.png" width="1142" height="230"></picture></strong><br><br><span style="font-size:18px"><strong>Via <em>'/omegamute reload'</em> you can reload all manual changes to the soundmap file located at '<em>/config/omegamute/soundmap.txt</em>':</strong></span><br><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/omega-mute/j.png" width="395" height="45"></picture><br><br><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/omega-mute/k.png" width="1142" height="64"></picture></p>
<p><br>------------------<br><br><span style="font-size:24px"><strong>You may freely use this mod in any modpack, as long as the download remains hosted within the CurseForge or Modrinth ecosystem.</strong></span><br><br><span style="font-size:18px"><a style="font-size:18px;color:#008000" href="https://serilum.com/" rel="nofollow">Serilum.com</a> contains an overview and more information on all mods available.</span><br><br><span style="font-size:14px">Comments are disabled as I'm unable to keep track of all the separate pages on each mod.</span><span style="font-size:14px"><br>For issues, ideas, suggestions or anything else there is the&nbsp;<a style="font-size:14px;color:#008000" href="https://serilum.com/url/issue-tracker" rel="nofollow">Github repo</a>. Thanks!</span><span style="font-size:6px"><br><br></span><a href="https://ricksouth.com/donate" rel="nofollow"><img src="https://github.com/Serilum/.cdn/raw/main/description/shields/donation_rounded.svg" alt="" width="306" height="50"></a></p>