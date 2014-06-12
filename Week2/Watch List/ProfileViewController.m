//
//  ProfileViewController.m
//  Watch List
//
//  Created by Christopher Rockwell on 6/5/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import "ProfileViewController.h"

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
    
    // retrieve values from Parse and display them to users
    PFQuery *query = [PFQuery queryWithClassName:@"movInfo"];
    [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        if (!error) {
            for (PFObject *object in objects) {
                hoursWatched = [[NSString stringWithFormat:@"%@",[object objectForKey:@"watchHrs"]] intValue];
                hrsLabel.text = [NSString stringWithFormat:@"%i",hoursWatched];
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
        [PFUser logOut];
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        ProfileViewController *loginView = [storyBoard instantiateViewControllerWithIdentifier:@"FirstView"];
        [self presentViewController:loginView animated:true completion:nil];
    } else {
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        ProfileViewController *watchListView = [storyBoard instantiateViewControllerWithIdentifier:@"WatchListView"];
        [self presentViewController:watchListView animated:true completion:nil];
    }
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
