//
//  AppDelegate.m
//  Watch List
//
//  Created by Christopher Rockwell on 6/4/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//
// Credit
// BZGFormField
// Copyright (c) 2013 Ben Guo
// License https://github.com/benzguo/BZGFormField/blob/master/LICENSE
// https://github.com/benzguo/BZGFormField
//

#import "AppDelegate.h"
#import <Parse/Parse.h>
#import "ViewController.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // set application id and ket to connect to Parse
    [Parse setApplicationId:@"roh7D1LeQ56iBto6N5KQMnkhKOaiy2mnZ8YlpQS8"
                  clientKey:@"QX4mWwY3D2uwGmpTxyQkj66uJnABUsbNfAM7dJCe"];
    
    PFUser *currentUser = [PFUser currentUser];
    
    if (currentUser != nil) {
        // show profile if user already signed in
        self.window = [[UIWindow alloc] initWithFrame:UIScreen.mainScreen.bounds];
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        
        ViewController *profileView = [storyBoard instantiateViewControllerWithIdentifier:@"ProfileView"];
        self.window.rootViewController = profileView;
    }
    // Override point for customization after application launch.
    return YES;
}
							
- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
