//
//  ProfileViewController.m
//  Watch List
//
//  Created by Christopher Rockwell on 6/5/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import "ProfileViewController.h"
Reachability *myNetworkReachability;
NetworkStatus myNetworkStatus;

@interface ProfileViewController ()

@end

@implementation ProfileViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    // white status bar
    [self setNeedsStatusBarAppearanceUpdate];
    
    __block int hoursWatched;
    
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    if ([[prefs valueForKey:@"firstRun"] isEqualToString:@"yes"]) {
        
        // retrieve values from Parse and display them to users
        PFQuery *query = [PFQuery queryWithClassName:@"movInfo"];
        [query whereKey:@"user" equalTo:[PFUser currentUser]];
        [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
            if (!error) {
                for (PFObject *object in objects) {
                    hoursWatched = [[NSString stringWithFormat:@"%@",[object objectForKey:@"watchHrs"]] intValue];
                    hrsLabel.text = [NSString stringWithFormat:@"%i",hoursWatched];
                    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
                    [prefs setObject:[NSString stringWithFormat:@"%i",hoursWatched] forKey:@"hours"];
                    [prefs setObject:[object objectId] forKey:@"hoursID"];
                    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT,
                                                             (unsigned long)NULL), ^(void) {
                        [self saveUserLocally];
                    });
                }
            } else {
                NSString *errorString = [error userInfo][@"error"];
                // show the errorString
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                message:errorString
                                                               delegate:nil
                                                      cancelButtonTitle:@"OK"
                                                      otherButtonTitles:nil];
                [alert show];
            }
            
        }];
        userLabel.text = [[PFUser currentUser] objectForKey:@"username"];
        emailLabel.text = [[PFUser currentUser] objectForKey:@"email"];
        NSString *favStr = [NSString stringWithFormat:@"%@",[[[PFUser currentUser] objectForKey:@"favMovie"]stringByReplacingOccurrencesOfString:@" " withString:@""]];
        if (![favStr isEqualToString:@""]) {
            favLabel.text = [[PFUser currentUser] objectForKey:@"favMovie"];
        } else {
            favLabel.text = @"None";
        }
        [prefs setObject:@"no" forKey:@"firstRun"];
    } else {
        NSLog(@"get local data");
        
        userLabel.text = [prefs valueForKey:@"username"];
        emailLabel.text = [prefs valueForKey:@"email"];
        
        NSString *favStr = [[NSString stringWithFormat:@"%@",[prefs valueForKey:@"fav"]] stringByReplacingOccurrencesOfString:@" " withString:@""];
        
        if (![favStr isEqualToString:@""]) {
            favLabel.text = [prefs valueForKey:@"fav"];
        } else {
            favLabel.text = @"None";
        }
        hrsLabel.text = [prefs valueForKey:@"hours"];
    }
    
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

// logout current users when logout button selected
-(IBAction)onClick:(id)sender {
    UIButton *button = (UIButton *)sender;
    if (button.tag == 0) {
            
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        ProfileViewController *editView = [storyBoard instantiateViewControllerWithIdentifier:@"EditProfileView"];
        [self presentViewController:editView animated:true completion:nil];
    } else if (button.tag == 1) {
        // log user out
        [PFUser logOut];
        NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
        [prefs setObject:nil forKey:@"username"];
        [prefs setObject:nil forKey:@"email"];
        [prefs setObject:nil forKey:@"fav"];
        [prefs setObject:nil forKey:@"hours"];
        [prefs setObject:nil forKey:@"hoursID"];
        [prefs setObject:nil forKey:@"title"];
        [prefs setObject:nil forKey:@"day"];
        [prefs setObject:nil forKey:@"time"];
        [prefs setObject:nil forKey:@"ids"];
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        ProfileViewController *loginView = [storyBoard instantiateViewControllerWithIdentifier:@"FirstView"];
        [self presentViewController:loginView animated:true completion:nil];
    } else {
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        ProfileViewController *watchListView = [storyBoard instantiateViewControllerWithIdentifier:@"WatchListView"];
        [self presentViewController:watchListView animated:true completion:nil];
    }
}

// save user info locally
- (void) saveUserLocally {
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    NSString *username = [NSString stringWithFormat:@"%@", userLabel.text];
    NSString *email = [NSString stringWithFormat:@"%@", emailLabel.text];
    NSString *fav = [NSString stringWithFormat:@"%@", favLabel.text];
    [prefs setObject:username forKey:@"username"];
    [prefs setObject:email forKey:@"email"];
    [prefs setObject:fav forKey:@"fav"];
    
    // get movies from parse and populate the table view
    PFQuery *query = [PFQuery queryWithClassName:@"itemInfo"];
    [query whereKey:@"user" equalTo:[PFUser currentUser]];
    [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        NSMutableArray *titlesArray = [[NSMutableArray alloc] init];
        NSMutableArray *daysArray = [[NSMutableArray alloc] init];
        NSMutableArray *timesArray = [[NSMutableArray alloc] init];
        NSMutableArray *idsArray = [[NSMutableArray alloc] init];
        
        if (!error) {
            for (PFObject *object in objects) {
                // add title to items array
                [titlesArray addObject:[object objectForKey:@"title"]];
                [daysArray addObject:[object objectForKey:@"day"]];
                [timesArray addObject:[object objectForKey:@"time"]];
                [idsArray addObject:object.objectId];
            }
            [prefs setObject:titlesArray forKey:@"title"];
            [prefs setObject:daysArray forKey:@"day"];
            [prefs setObject:timesArray forKey:@"time"];
            [prefs setObject:idsArray forKey:@"ids"];
            NSLog(@"Done Saving");
        } else {
            NSString *errorString = [error userInfo][@"error"];
            // show the errorString
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                            message:errorString
                                                           delegate:nil
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];
        }
        
    }];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
