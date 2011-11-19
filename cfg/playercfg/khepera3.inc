
# Desc; Device definitions for K-Team robots.
# Author: Jean-Pierre de la Croix
# Date: 19 Nov 2011

define khepera3_ir ranger
(
  scount 9 # the number of IR sensors
  # Note: ignoring the two floor facing IR sensors

  # define the pose of each sensor [xpos ypose headng]
  spose[0] [ -0.038  0.048  128 ]
  spose[1] [  0.019  0.064   75 ]
  spose[2] [  0.050  0.050   42 ]
  spose[3] [  0.070  0.017   13 ]
  spose[4] [  0.070 -0.017  -13 ]
  spose[5] [  0.050 -0.050  -42 ]
  spose[6] [  0.019 -0.064  -75 ]
  spose[7] [ -0.038 -0.048 -128 ]
  spose[8] [ -0.048  0.0    180 ]

  # define the field of view of each sensor [range_min range_max view_angle]
  sview [ 0.02 2 20 ]

  # define the size of each sensor [xsize ysize] in meters
  ssview [ 0.01 0.05 ]
)

define khepera3_base position 
(
  #color "gray"			# Default color.
  drive "diff"		 	# Differential steering model.
  gui_nose 1                  	# Draw a nose on the robot so we can see which way it points
  obstacle_return 1           	# Can hit things.
  laser_return 1                 # reflects laser beams
  ranger_return 1             	# reflects sonar beams
  blob_return 1               	# Seen by blobfinders  
  fiducial_return 1           	# Seen as "1" fiducial finders

  localization "gps"         	  
  localization_origin [0 0 0 0] 	# Start odometry at (0, 0, 0).

  # alternative odometric localization with simple error model
  # localization "odom"         		# Change to "gps" to have impossibly perfect, global odometry
  #  odom_error [ 0.05 0.05 0.1 ]   	# Odometry error or slip in X, Y and Theta
                                   	# (Uniform random distribution)   

  # actual size
  size [0.122 0.128 0.07] # sizes from MobileRobots' web site

  # the pioneer's center of rotation is offset from its center of area
  origin [0 0 0 0]

  # estimated mass in KG
  mass 5.0 

  khepera3_ir( pose [ 0 0 -0.03 0] )
)

define khepera3 khepera3_base
(  
  # simplified Body shape:
  block( 
    points 12
    point[0] [-0.024 0.064]
    point[1] [0.033 0.064]
    point[2] [0.057 0.043]
    point[3] [0.074 0.010]
    point[4] [0.074 -0.010]
    point[5] [0.057 -0.043]
    point[6] [0.033 -0.064]
    point[7] [-0.025 -0.064]
    point[8] [-0.042 -0.043]
    point[9] [-0.048 -0.010]
    point[10] [-0.048 0.010]
    point[11] [-0.042 0.043]
    z [0 0.07]
    color "gray"
  )

  block(
    points 7
    point[0] [-0.031 0.043]
    point[1] [-0.031 -0.043]
    point[2] [ 0.033 -0.043]
    point[3] [ 0.052 -0.021]
    point[4] [ 0.057 0]
    point[5] [ 0.052 0.021]
    point[6] [ 0.033 0.043]
    z [0 0.08]
    color "black"
  )


)
