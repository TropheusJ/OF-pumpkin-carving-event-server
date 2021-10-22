package io.github.tropheusj.backend;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class Constants {
	// teleports
	public static final Utils.TPTarget
			SPAWN = new Utils.TPTarget(-12.5f, 7.1f, 28.5f, 90, 0),
			ROUND_2_SPAWN = new Utils.TPTarget(14.5f, 4, 4.5f, 90, 0),
			COMMENTATOR_ROOM = new Utils.TPTarget(-66.5f, 22.5f, 28.5f, 90, 0),
			CAMERA_MANSION = new Utils.TPTarget(-8.5f, 10, 28.5f, 90, 0),
			CAMERA_PUMPKINS = new Utils.TPTarget(25.5f, 5, 29.5f, 180, 0),
	// podium places
			FIRST = new Utils.TPTarget(-57.5f, 12, 28.5f, -90, 0),
			SECOND = new Utils.TPTarget(-57.5f, 11, 29.5f, -90, 0),
			THIRD = new Utils.TPTarget(-57.5f, 10, 27.5f, -90, 0),
			SPECTATORS = new Utils.TPTarget(-46.5f, 7.1f, 28.5f, 90, 0);

	// pumpkins
	public static final BlockPos
			FIRST_PUMPKIN = new BlockPos(20, 5, 20),
			FIRST_ROUND_2_PUMPKIN = new BlockPos(6, 7, 11);

	// incoming packets from camera client
	public static final Identifier
			COMMENTATOR_TP = new Identifier("client", "commentator_tp"),
			PUMPKINS_TP = new Identifier("client", "pumpkins_tp"),
			MANSION_TP = new Identifier("client", "mansion_tp");

	// players
	public static final String[] TP_BLACKLIST = new String[] {
			"25b54bf7-b250-4d93-8988-a73e9610a1d6",
			"6dde21bc-0d5a-4f98-9277-6b3aa4ebcece",
			"f24039db-ec42-44f0-9e69-d816c3eacb25",
			"272525ed-5125-41d0-aff8-03bc99cb6a24"
	};
	public static final String[][] SKULL_PROFILES = new String[][] {
			// name, UUID
			// optistaff
			new String[] {"sp614x", "07a4bbac-8d56-4cee-8e06-3e44ccc31fc3"},
			new String[] {"KaiAF", "a6eb2491-7365-4649-a795-2f032c9919f0"},
			new String[] {"Jiingy", "42643840-8882-4a0f-8d2e-f7cf48e9c347"},
			new String[] {"MrCheeze445", "25b54bf7-b250-4d93-8988-a73e9610a1d6"},
			new String[] {"Jorkles", "3dcc4b08-9410-4b79-ab5f-3946cd870ac5"},
			new String[] {"TropheusJay", "272525ed-5125-41d0-aff8-03bc99cb6a24"},
			new String[] {"Siuolplex", "07cb3dfd-ee1d-4ecf-b5b5-f70d317a82eb"},
			new String[] {"graac", "6dde21bc-0d5a-4f98-9277-6b3aa4ebcece"},
			new String[] {"MainGameCentral", "f24039db-ec42-44f0-9e69-d816c3eacb25"},
			new String[] {"ZenithKnight", "3d60771c-b620-4719-bf44-3b038bcbf246"},
			new String[] {"KyriaBirb", "2a7f7951-3694-49f5-97e1-417365177ce2"},
			new String[] {"ZenW", "10710270-4277-4937-9a89-bf317dd15f4d"},
			new String[] {"Pimz", "376436af-ae9e-4855-ac12-9a2edde5d9fc"},
			new String[] {"sk18r", "28da2783-a1bc-4605-9a08-79b806820525"},
			new String[] {"justseska", "726d024c-1cb6-407f-9d3f-ef806d1954b3"},
			new String[] {"Tyilak13", "92d532b4-eb85-4cf6-9a66-29f27bf35d07"},
			new String[] {"ewanhowell5195", "d31e5dca-fafb-4e5b-bb10-b14dff9dbc8a"},
			new String[] {"Ramens", "d2ad277f-144b-4140-bc1f-2606926d2de6"},
			new String[] {"SithRevan", "26c9c628-b603-4b97-b144-5a29676aa826"},
			new String[] {"MrGlockenspiel", "60be1dc7-1fae-4b62-ada9-7bc9a556afa5"},
			new String[] {"ASININJA", "552ea463-cfd7-43f2-8728-290375984f61"},
			new String[] {"Stifya", "da2b241a-4b03-4bc5-a184-336fbc2a6c47"},
			new String[] {"OscarThePupper", "be5d3303-e9e6-4e86-af92-c9e1ac34fc5b"},
			// other
			new String[] {"filefolder3", "aaf11ef0-e29e-4ecf-950b-b078083f56b8"},
			new String[] {"jckt", "891d59a8-e018-4030-944b-3345342a0189"},
			new String[] {"Bisou", "12283116-6770-4193-a92f-c26a31a3455d"},
			new String[] {"OptiBotLite", "b9e98ac2-2df0-4ec5-aafe-66d364c52f9b"},
	};
}
